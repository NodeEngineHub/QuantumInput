package ca.nodeengine.quantum.context;

import ca.nodeengine.quantum.action.DefaultActionMap;
import ca.nodeengine.quantum.action.DigitalBinding;
import ca.nodeengine.quantum.api.action.ActionEvent;
import ca.nodeengine.quantum.api.event.InputEvent;
import ca.nodeengine.quantum.api.event.InputEventType;
import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.event.InputListener;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to make sure action map events work correctly with context.
 *
 * @author FX
 */
class ActionMapContextTest {

    @Test
    void testActionEventOnlyFiredWhenContextActive() {
        DefaultActionMap actionMap = new DefaultActionMap();
        actionMap.add(new DigitalBinding(null, "Jump", 32));

        List<ActionEvent> receivedEvents = new ArrayList<>();
        actionMap.addActionListener("test", receivedEvents::add);
        InputListener bridge = actionMap.createInputListener();

        DefaultInputContextManager manager = new DefaultInputContextManager();
        DefaultInputContext context = new DefaultInputContext("Game", actionMap, 0);

        // Scenario 1: Context not in manager at all, but we set a predicate manually
        // (By default it returns true if no predicate is set, but here we simulate what happens if it's "managed" but not active)
        actionMap.setActivePredicate(() -> false);
        bridge.onInputEvent(new MockInputEvent(32, InputEventType.KEY_PRESSED));
        assertTrue(receivedEvents.isEmpty(), "Events should not fire if action map is not active");

        // Scenario 2: Context registered but not pushed (not active)
        // registerContext should set the predicate to check if it's in the active list
        manager.registerContext(context);
        bridge.onInputEvent(new MockInputEvent(32, InputEventType.KEY_PRESSED));
        assertTrue(receivedEvents.isEmpty(), "Events should not fire if action map is registered but not active");

        // Scenario 3: Context pushed (active)
        manager.pushContext(context);
        bridge.onInputEvent(new MockInputEvent(32, InputEventType.KEY_PRESSED));
        assertFalse(receivedEvents.isEmpty(), "Events SHOULD fire when context is active");
        receivedEvents.clear();

        // Scenario 4: Context popped (no longer active)
        manager.popContext("Game");
        bridge.onInputEvent(new MockInputEvent(32, InputEventType.KEY_PRESSED));
        assertTrue(receivedEvents.isEmpty(), "Events should not fire after context is popped");
    }

    @Test
    void testMultipleContexts() {
        DefaultActionMap uiMap = new DefaultActionMap();
        uiMap.add(new DigitalBinding(null, "Click", 1));
        List<ActionEvent> uiEvents = new ArrayList<>();
        uiMap.addActionListener("ui", uiEvents::add);
        InputListener uiBridge = uiMap.createInputListener();

        DefaultActionMap gameMap = new DefaultActionMap();
        gameMap.add(new DigitalBinding(null, "Fire", 1));
        List<ActionEvent> gameEvents = new ArrayList<>();
        gameMap.addActionListener("game", gameEvents::add);
        InputListener gameBridge = gameMap.createInputListener();

        DefaultInputContextManager manager = new DefaultInputContextManager();
        manager.pushContext(new DefaultInputContext("UI", uiMap, 100));
        manager.pushContext(new DefaultInputContext("Game", gameMap, 0));

        // Both are active
        InputEvent clickEvent = new MockInputEvent(1, InputEventType.KEY_PRESSED);
        uiBridge.onInputEvent(clickEvent);
        gameBridge.onInputEvent(clickEvent);

        assertEquals(1, uiEvents.size());
        assertEquals(1, gameEvents.size());

        uiEvents.clear();
        gameEvents.clear();

        // Pop UI
        manager.popContext("UI");
        uiBridge.onInputEvent(clickEvent);
        gameBridge.onInputEvent(clickEvent);

        assertTrue(uiEvents.isEmpty(), "UI events should not fire when UI context is inactive");
        assertEquals(1, gameEvents.size(), "Game events should still fire");
    }

    private record MockInputEvent(int code, InputEventType type) implements InputEvent {
        @Override public InputDevice device() { return null; }
        @Override public float value1() { return 0; }
        @Override public float value2() { return 0; }
    }
}
