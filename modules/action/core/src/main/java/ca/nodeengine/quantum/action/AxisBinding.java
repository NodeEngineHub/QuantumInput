package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.event.InputEvent;
import ca.nodeengine.quantum.api.event.InputEventType;
import ca.nodeengine.quantum.api.state.GlobalInputState;
import ca.nodeengine.quantum.api.state.InputState;
import ca.nodeengine.quantum.api.state.PerDeviceInputState;
import org.jspecify.annotations.Nullable;

/**
 * An analog axis binding.
 *
 * @author FX
 */
public final class AxisBinding implements ActionBinding {
    private final @Nullable InputDevice device;
    private final String action;
    private final int axis;
    private final float deadzone;
    private final float scale;

    public AxisBinding(@Nullable InputDevice device, String action, int axis) {
        this(device, action, axis, 0.05F, 1.0F);
    }

    public AxisBinding(@Nullable InputDevice device, String action, int axis, float deadzone, float scale) {
        this.device = device;
        this.action = action;
        this.axis = axis;
        this.deadzone = deadzone;
        this.scale = scale;
    }

    @Override
    public String action() {
        return action;
    }

    @Override
    public boolean matches(InputState state) {
        return Math.abs(value(state)) > 0;
    }

    @Override
    public float value(InputState state) {
        float val = 0;
        if (state instanceof GlobalInputState globalState) {
            val = globalState.getAxis(axis);
        } else if (state instanceof PerDeviceInputState perDeviceState && device != null) {
            val = perDeviceState.getAxis(device, axis);
        }
        if (Math.abs(val) < deadzone) {
            return 0;
        }
        return val * scale;
    }

    @Override
    public boolean isTriggeredBy(InputEvent event) {
        if (device != null && !device.equals(event.device())) {
            return false;
        }
        return event.type() == InputEventType.AXIS_CHANGED && event.code() == axis;
    }

    @Override
    public float value(InputEvent event) {
        if (isTriggeredBy(event)) {
            float val = event.value1();
            if (Math.abs(val) < deadzone) {
                return 0;
            }
            return val * scale;
        }
        return 0;
    }

    @Override
    public boolean isPressed(InputState state) {
        // For analog, "pressed" could mean "just crossed the deadzone"
        // But since we don't have easy access to previous axis value here, 
        // we'll leave it as false or implement if needed.
        return false;
    }

    @Override
    public int triggerCode() {
        return axis;
    }
}
