package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.action.ActionBinding;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link DefaultActionMap}
 *
 * @author FX
 */
class DefaultActionMapTest {

    @Test
    void addAndGetBindings() {
        DefaultActionMap actionMap = new DefaultActionMap();
        String action = "Jump";
        ActionBinding binding = new DigitalBinding(null, action, 32);

        actionMap.addBinding(binding);

        List<ActionBinding> bindings = actionMap.getBindings(action);
        assertEquals(1, bindings.size());
        assertEquals(binding, bindings.getFirst());
    }

    @Test
    void getAllBindings() {
        DefaultActionMap actionMap = new DefaultActionMap();
        String action1 = "Action1";
        String action2 = "Action2";
        ActionBinding binding1 = new DigitalBinding(null, action1, 1);
        ActionBinding binding2 = new DigitalBinding(null, action2, 2);

        actionMap.addBinding(binding1);
        actionMap.addBinding(binding2);

        assertEquals(2, actionMap.getAllBindings().size());
    }
}
