package ca.nodeengine.quantum.context;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.action.InputAction;
import ca.nodeengine.quantum.api.action.ActionMap;
import ca.nodeengine.quantum.api.state.InputState;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContextActionInputTest {

    @Test
    void getValueFromContexts() {
        InputAction jump = mock(InputAction.class);
        when(jump.name()).thenReturn("Jump");
        
        MockActionMap uiMap = new MockActionMap();
        uiMap.addBindingInternal(new ConstantBinding(jump, 1.0f));
        
        MockActionMap gameMap = new MockActionMap();
        gameMap.addBindingInternal(new ConstantBinding(jump, 0.5f));

        DefaultInputContextManager manager = new DefaultInputContextManager();
        manager.pushContext(new DefaultInputContext("UI", uiMap, 100));
        manager.pushContext(new DefaultInputContext("Game", gameMap, 0));

        ContextActionInput input = new ContextActionInput(new SimpleMockInputState(), manager);

        // Should take from UI context because it has higher priority and contains the action
        assertEquals(1.0f, input.getValue(jump));

        manager.popContext("UI");
        // Now should take from Game context
        assertEquals(0.5f, input.getValue(jump));
    }

    @Test
    void getValueReturnsZeroIfActionNotFoundInAnyContext() {
        InputAction unknown = new MockInputAction("Unknown");
        DefaultInputContextManager manager = new DefaultInputContextManager();
        manager.pushContext(new DefaultInputContext("Game", new MockActionMap(), 0));

        ContextActionInput input = new ContextActionInput(new SimpleMockInputState(), manager);
        assertEquals(0.0f, input.getValue(unknown));
    }

    static class SimpleMockInputState implements InputState {
        @Override
        public void update() {}

        @Override
        public void reset() {}
    }

    static class MockActionMap implements ActionMap {
        private final Map<String, List<ActionBinding>> bindings = new HashMap<>();
        private final Map<String, InputAction> actions = new HashMap<>();

        void addBindingInternal(ActionBinding binding) {
            bindings.computeIfAbsent(binding.action().name(), k -> new ArrayList<>()).add(binding);
            actions.put(binding.action().name(), binding.action());
        }

        @Override
        public InputAction getAction(String name) {
            return actions.get(name);
        }

        @Override
        public List<ActionBinding> getBindings(InputAction action) {
            return bindings.getOrDefault(action.name(), List.of());
        }
    }

    record MockInputAction(String name) implements InputAction {
    }

    static class ConstantBinding implements ActionBinding {
        private final InputAction action;
        private final float value;

        ConstantBinding(InputAction action, float value) {
            this.action = action;
            this.value = value;
        }

        @Override
        public InputDevice device() {
            return null;
        }

        @Override
        public InputAction action() {
            return action;
        }

        @Override
        public boolean matches(InputState state) {
            return value > 0.5F;
        }

        @Override
        public float value(InputState state) {
            return value;
        }
    }
}
