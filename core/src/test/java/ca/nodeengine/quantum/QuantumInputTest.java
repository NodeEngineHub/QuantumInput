package ca.nodeengine.quantum;

import ca.nodeengine.quantum.action.DefaultInputAction;
import ca.nodeengine.quantum.action.DefaultActionMap;
import ca.nodeengine.quantum.action.DigitalBinding;
import ca.nodeengine.quantum.api.InputCode;
import ca.nodeengine.quantum.api.action.InputAction;
import ca.nodeengine.quantum.context.DefaultInputContext;
import ca.nodeengine.quantum.event.KeyPressedEvent;
import ca.nodeengine.quantum.event.KeyReleasedEvent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantumInputTest {

    @Test
    public void testBasicInputFlow() {
        QuantumInput input = new QuantumInput();
        InputCode keyA = new DefaultInputCode(65, "KeyA");
        InputAction jumpAction = new DefaultInputAction("Jump");
        
        DefaultActionMap actionMap = new DefaultActionMap();
        actionMap.addBinding(new DigitalBinding(jumpAction, keyA));
        
        DefaultInputContext context = new DefaultInputContext("Game", actionMap, 0);
        input.contexts().pushContext(context);

        // --- Frame 1: Initial state ---
        input.poll();
        assertFalse(input.rawState().isDown(keyA));
        assertFalse(input.actions().isDown(jumpAction));

        // --- Frame 2: Press KeyA ---
        input.poll();
        input.eventListener().onInputEvent(new KeyPressedEvent(keyA, System.nanoTime()));
        input.updateActions();
        
        assertTrue(input.rawState().isDown(keyA), "KeyA should be down");
        
        assertTrue(input.actions().isDown(jumpAction), "Jump action should be down");

        // --- Frame 3: KeyA still down ---
        input.poll();
        input.updateActions();
        
        assertTrue(input.rawState().isDown(keyA));
        
        assertTrue(input.actions().isDown(jumpAction));

        // --- Frame 4: Release KeyA ---
        input.poll();
        input.eventListener().onInputEvent(new KeyReleasedEvent(keyA, System.nanoTime()));
        input.updateActions();
        
        assertFalse(input.rawState().isDown(keyA));
        
        assertFalse(input.actions().isDown(jumpAction));
    }
    
    @Test
    public void testContextPriority() {
        QuantumInput input = new QuantumInput();
        InputCode keyA = new DefaultInputCode(65, "KeyA");
        InputCode keyB = new DefaultInputCode(66, "KeyB");
        InputAction fireAction = new DefaultInputAction("Fire");
        
        // Context 1: Normal gameplay (Fire = KeyA)
        DefaultActionMap map1 = new DefaultActionMap();
        map1.addBinding(new DigitalBinding(fireAction, keyA));
        input.contexts().pushContext(new DefaultInputContext("Gameplay", map1, 10));
        
        // Context 2: Menu (Fire = KeyB)
        DefaultActionMap map2 = new DefaultActionMap();
        map2.addBinding(new DigitalBinding(fireAction, keyB));
        input.contexts().pushContext(new DefaultInputContext("Menu", map2, 100)); // Higher priority

        input.eventListener().onInputEvent(new KeyPressedEvent(keyA, System.nanoTime()));
        input.poll();
        input.updateActions();
        assertFalse(input.actions().isDown(fireAction), "Fire should NOT match KeyA because Menu context has higher priority and expects KeyB");

        input.eventListener().onInputEvent(new KeyPressedEvent(keyB, System.nanoTime()));
        input.poll();
        input.updateActions();
        assertTrue(input.actions().isDown(fireAction), "Fire should match KeyB from Menu context");
        
        // Remove Menu context
        input.contexts().popContext("Menu");
        input.poll();
        input.updateActions();
        assertTrue(input.actions().isDown(fireAction), "Fire should now match KeyA from Gameplay context");
    }
}
