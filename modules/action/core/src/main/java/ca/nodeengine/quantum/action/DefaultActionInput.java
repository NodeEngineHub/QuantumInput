package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.state.InputState;
import ca.nodeengine.quantum.api.action.*;
import ca.nodeengine.quantum.api.context.InputContext;
import ca.nodeengine.quantum.api.context.InputContextManager;

import java.util.List;

public class DefaultActionInput implements ActionInput {
    private final InputState state;
    private final InputContextManager contextManager;

    public DefaultActionInput(InputState state, InputContextManager contextManager) {
        this.state = state;
        this.contextManager = contextManager;
    }

    public void update() {

    }

    @Override
    public boolean isDown(InputAction action) {
        return getValue(action) > 0.5f;
    }

    @Override
    public float getValue(InputAction action) {
        List<InputContext> activeContexts = contextManager.getActiveContexts();
        for (InputContext context : activeContexts) {
            ActionMap actionMap = context.actionMap();
            List<ActionBinding> bindings = actionMap.getBindings(action);
            if (!bindings.isEmpty()) {
                float max = 0.0f;
                for (ActionBinding binding : bindings) {
                    max = Math.max(max, binding.value(state));
                }
                return max;
            }
        }
        return 0.0f;
    }
}
