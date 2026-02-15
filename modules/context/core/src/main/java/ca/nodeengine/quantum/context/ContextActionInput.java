package ca.nodeengine.quantum.context;

import ca.nodeengine.quantum.api.state.InputState;
import ca.nodeengine.quantum.api.action.*;
import ca.nodeengine.quantum.api.context.InputContext;
import ca.nodeengine.quantum.api.context.InputContextManager;
import java.util.List;

public class ContextActionInput implements ActionInput {
    private final InputState state;
    private final InputContextManager contextManager;

    public ContextActionInput(InputState state, InputContextManager contextManager) {
        this.state = state;
        this.contextManager = contextManager;
    }

    public void update() {

    }

    @Override
    public boolean isDown(String action) {
        return getValue(action) > 0.5F;
    }

    @Override
    public float getValue(String action) {
        List<InputContext> activeContexts = contextManager.getActiveContexts();
        for (InputContext context : activeContexts) {
            ActionMap actionMap = context.actionMap();
            List<ActionBinding> bindings = actionMap.getBindings(action);
            if (!bindings.isEmpty()) {
                float max = 0F;
                for (ActionBinding binding : bindings) {
                    max = Math.max(max, binding.value(state));
                }
                return max;
            }
        }
        return 0F;
    }
}
