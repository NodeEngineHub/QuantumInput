package ca.nodeengine.quantum.context;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.InputType;
import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.action.ActionListener;
import ca.nodeengine.quantum.api.action.ActionMap;
import ca.nodeengine.quantum.api.event.InputEvent;
import ca.nodeengine.quantum.api.state.InputState;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the context-aware inputs.
 *
 * @author FX
 */
class ContextActionInputTest {

    @Test
    void getValueFromContexts() {
        String jump = "Jump";
        
        MockActionMap uiMap = new MockActionMap();
        uiMap.addBindingInternal(new ConstantBinding(jump, 1.0F));
        
        MockActionMap gameMap = new MockActionMap();
        gameMap.addBindingInternal(new ConstantBinding(jump, 0.5F));

        DefaultInputContextManager manager = new DefaultInputContextManager();
        manager.pushContext(new DefaultInputContext("UI", uiMap, 100));
        manager.pushContext(new DefaultInputContext("Game", gameMap, 0));

        InputState inputState = new SimpleMockInputState();

        // Should take from UI context because it has higher priority and contains the action
        assertEquals(1.0F, manager.getValue(inputState, jump));

        manager.popContext("UI");
        // Now should take from Game context
        assertEquals(0.5F, manager.getValue(inputState, jump));
    }

    @Test
    void getValueReturnsZeroIfActionNotFoundInAnyContext() {
        String unknown = "Unknown";
        DefaultInputContextManager manager = new DefaultInputContextManager();
        manager.pushContext(new DefaultInputContext("Game", new MockActionMap(), 0));

        assertEquals(0F, manager.getValue(new SimpleMockInputState(), unknown));
    }

    static class SimpleMockInputState implements InputState {
        @Override
        public void update() {}

        @Override
        public void reset() {}
    }

    static class MockActionMap implements ActionMap {
        private final Map<String, List<ActionBinding>> bindings = new HashMap<>();

        void addBindingInternal(ActionBinding binding) {
            bindings.computeIfAbsent(binding.action(), _ -> new ArrayList<>()).add(binding);
        }

        @Override
        public List<ActionBinding> getBindings(String action) {
            return bindings.getOrDefault(action, List.of());
        }

        @Override
        public ActionMap add(String actionName, int code) {
            return this;
        }

        @Override
        public ActionMap add(String actionName, @Nullable InputDevice device, int code) {
            return this;
        }

        @Override
        public ActionMap add(String actionName, @Nullable InputDevice device, int code, InputType type) {
            return this;
        }

        @Override
        public ActionMap add(ActionBinding binding) {
            return this;
        }

        @Override
        public ActionBinding createBinding(String actionName, int code) {
            return null;
        }

        @Override
        public ActionBinding createBinding(String actionName, @Nullable InputDevice device, int code) {
            return null;
        }

        @Override
        public ActionBinding createBinding(String actionName, @Nullable InputDevice device, int code, InputType type) {
            return null;
        }

        @Override
        public ActionBinding createCompositeBinding(String actionName, ActionBinding... components) {
            return null;
        }

        @Override
        public void clearBindings(String action) {}

        @Override
        public Map<String, List<ActionBinding>> getAllBindings() {
            return Map.of();
        }

        @Override
        public void addActionListener(String name, ActionListener listener) {}

        @Override
        public void removeActionListener(String name) {}

        @Override
        public Collection<ActionListener> getActionListeners() {
            return List.of();
        }
    }

    static class ConstantBinding implements ActionBinding {
        private final String action;
        private final float value;

        ConstantBinding(String action, float value) {
            this.action = action;
            this.value = value;
        }

        @Override
        public String action() {
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

        @Override
        public boolean isTriggeredBy(InputEvent event) {
            return false;
        }

        @Override
        public float value(InputEvent event) {
            return 0;
        }
    }
}
