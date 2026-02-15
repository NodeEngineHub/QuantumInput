package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.state.InputState;
import ca.nodeengine.quantum.api.state.GlobalInputState;
import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.action.InputAction;
import ca.nodeengine.quantum.api.state.PerDeviceInputState;
import org.jspecify.annotations.Nullable;

public final class DigitalBinding implements ActionBinding {
    private final @Nullable InputDevice device;
    private final InputAction action;
    private final int code;

    public DigitalBinding(@Nullable InputDevice inputDevice, InputAction action, int code) {
        this.device = inputDevice;
        this.action = action;
        this.code = code;
    }

    @Override
    public @Nullable InputDevice device() {
        return device;
    }

    @Override
    public InputAction action() {
        return action;
    }

    @Override
    public boolean matches(InputState state) {
        if (state instanceof GlobalInputState globalState) {
            return globalState.isKeyHeld(code) || globalState.isButtonHeld(code);
        } else if (state instanceof PerDeviceInputState perDeviceState && device != null) {
            return perDeviceState.isKeyHeld(device, code) || perDeviceState.isButtonHeld(device, code);
        }
        return false;
    }

    @Override
    public float value(InputState state) {
        return matches(state) ? 1.0f : 0.0f;
    }
}
