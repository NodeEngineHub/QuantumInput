package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.action.InputAction;
import ca.nodeengine.quantum.api.state.InputState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultActionInputTest {

    @Test
    void getValueFromBindings() {
        InputAction action = new DefaultInputAction("Jump");
        DefaultActionMap actionMap = new DefaultActionMap();
        SimpleMockInputState state = new SimpleMockInputState();
        
        ActionBinding binding = new ActionBinding() {
            @Override
            public InputDevice device() {
                return null;
            }

            @Override
            public InputAction action() {
                return action;
            }

            @Override public boolean matches(InputState s) {
                return value(s) > 0.5F;
            }

            @Override
            public float value(InputState s) {
                return ((SimpleMockInputState)s).value;
            }
        };
        actionMap.addBinding(binding);

        DefaultActionInput actionInput = new DefaultActionInput(state, actionMap);
        
        state.value = 0.8f;
        assertEquals(0.8f, actionInput.getValue(action));
        assertTrue(actionInput.isDown(action));

        state.value = 0.3f;
        assertEquals(0.3f, actionInput.getValue(action));
        assertFalse(actionInput.isDown(action));
    }

    @Test
    void getValueReturnsMaxOfBindings() {
        InputAction action = new DefaultInputAction("Jump");
        DefaultActionMap actionMap = new DefaultActionMap();
        SimpleMockInputState state = new SimpleMockInputState();

        actionMap.addBinding(new ActionBinding() {
            @Override
            public InputDevice device() {
                return null;
            }

            @Override
            public InputAction action() {
                return action;
            }

            @Override
            public boolean matches(InputState s) {
                return true;
            }

            @Override
            public float value(InputState s) {
                return 0.5F;
            }
        });
        actionMap.addBinding(new ActionBinding() {
            @Override
            public InputDevice device() {
                return null;
            }

            @Override
            public InputAction action() {
                return action;
            }

            @Override
            public boolean matches(InputState s) {
                return true;
            }

            @Override
            public float value(InputState s) {
                return 0.9F;
            }
        });

        DefaultActionInput actionInput = new DefaultActionInput(state, actionMap);
        assertEquals(0.9f, actionInput.getValue(action));
    }

    static class SimpleMockInputState implements InputState {
        float value;

        @Override
        public void update() {}

        @Override
        public void reset() {}
    }
}
