package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.state.InputState;
import ca.nodeengine.quantum.api.action.*;

import java.util.List;

public class DefaultActionInput implements ActionInput {
    private final InputState state;
    private final ActionMap actionMap;

    public DefaultActionInput(InputState state, ActionMap actionMap) {
        this.state = state;
        this.actionMap = actionMap;
    }

    public void update() {

    }

    @Override
    public boolean isDown(InputAction action) {
        return getValue(action) > 0.5F;
    }

    @Override
    public float getValue(InputAction action) {
        List<ActionBinding> bindings = actionMap.getBindings(action);
        if (!bindings.isEmpty()) {
            float max = 0;
            for (ActionBinding binding : bindings) {
                max = Math.max(max, binding.value(state));
            }
            return max;
        }
        return 0.0f;
    }
}
