package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.state.InputState;
import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.action.InputAction;

public final class DigitalBinding implements ActionBinding {
    private final InputAction action;
    private final int code;

    public DigitalBinding(InputAction action, int code) {
        this.action = action;
        this.code = code;
    }

    @Override
    public InputAction action() {
        return action;
    }

    @Override
    public boolean matches(InputState state) {
        return state.isDown(code);
    }

    @Override
    public float value(InputState state) {
        return state.isDown(code) ? 1.0f : 0.0f;
    }
}
