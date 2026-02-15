package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.event.InputEvent;
import ca.nodeengine.quantum.api.state.InputState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link DefaultActionInput}
 *
 * @author FX
 */
class DefaultActionInputTest {

    @Test
    void getValueFromBindings() {
        String action = "Jump";
        DefaultActionMap actionMap = new DefaultActionMap();
        SimpleMockInputState state = new SimpleMockInputState();
        
        ActionBinding binding = new ActionBinding() {
            @Override
            public String action() {
                return action;
            }

            @Override public boolean matches(InputState s) {
                return value(s) > 0.5F;
            }

            @Override
            public float value(InputState s) {
                return ((SimpleMockInputState)s).value;
            }

            @Override
            public boolean isTriggeredBy(InputEvent event) {
                return false;
            }

            @Override
            public float value(InputEvent event) {
                return 0;
            }
        };
        actionMap.add(binding);

        DefaultActionInput actionInput = new DefaultActionInput(state, actionMap);
        
        state.value = 0.8F;
        assertEquals(0.8F, actionInput.getValue(action));
        assertTrue(actionInput.isActive(action));

        state.value = 0.3F;
        assertEquals(0.3F, actionInput.getValue(action));
        assertFalse(actionInput.isActive(action));
    }

    @Test
    void getValueReturnsMaxOfBindings() {
        String action = "Jump";
        DefaultActionMap actionMap = new DefaultActionMap();
        SimpleMockInputState state = new SimpleMockInputState();

        actionMap.add(new ActionBinding() {
            @Override
            public String action() {
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

            @Override
            public boolean isTriggeredBy(InputEvent event) {
                return false;
            }

            @Override
            public float value(InputEvent event) {
                return 0;
            }
        });
        actionMap.add(new ActionBinding() {
            @Override
            public String action() {
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

            @Override
            public boolean isTriggeredBy(InputEvent event) {
                return false;
            }

            @Override
            public float value(InputEvent event) {
                return 0;
            }
        });

        DefaultActionInput actionInput = new DefaultActionInput(state, actionMap);
        assertEquals(0.9F, actionInput.getValue(action));
    }

    static class SimpleMockInputState implements InputState {
        float value;

        @Override
        public void update() {}

        @Override
        public void reset() {}
    }
}
