package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.action.InputAction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultActionMapTest {

    @Test
    void addAndGetBindings() {
        DefaultActionMap actionMap = new DefaultActionMap();
        InputAction action = new DefaultInputAction("Jump");
        ActionBinding binding = new DigitalBinding(null, action, 32);

        actionMap.addBinding(binding);

        assertEquals(action, actionMap.getAction("Jump"));
        List<ActionBinding> bindings = actionMap.getBindings(action);
        assertEquals(1, bindings.size());
        assertEquals(binding, bindings.get(0));
    }

    @Test
    void getActionThrowsWhenNotFound() {
        DefaultActionMap actionMap = new DefaultActionMap();
        assertThrows(IllegalArgumentException.class, () -> actionMap.getAction("NonExistent"));
    }

    @Test
    void getAllBindings() {
        DefaultActionMap actionMap = new DefaultActionMap();
        InputAction action1 = new DefaultInputAction("Action1");
        InputAction action2 = new DefaultInputAction("Action2");
        ActionBinding binding1 = new DigitalBinding(null, action1, 1);
        ActionBinding binding2 = new DigitalBinding(null, action2, 2);

        actionMap.addBinding(binding1);
        actionMap.addBinding(binding2);

        assertEquals(2, actionMap.getAllBindings().size());
    }
}
