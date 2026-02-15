package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.InputType;
import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.state.InputState;
import ca.nodeengine.quantum.api.state.GlobalInputState;
import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.event.InputEvent;
import ca.nodeengine.quantum.api.event.InputEventType;
import ca.nodeengine.quantum.api.state.PerDeviceInputState;
import org.jspecify.annotations.Nullable;

/**
 * A digital input binding.
 * <p>
 * This binding is either active (value 1.0) or inactive (value 0.0) based on
 * whether a key or button is held.
 * </p>
 *
 * @author FX
 */
public final class DigitalBinding implements ActionBinding {

    /** The device restricted to, or {@code null} for any device. */
    private final @Nullable InputDevice device;
    /** The action name. */
    private final String action;
    /** The input code. */
    private final int code;
    /** The type of input. */
    private final InputType type;

    /**
     * Constructs a new DigitalBinding.
     *
     * @param inputDevice The device to restrict to, or {@code null} for any.
     * @param action      The action name.
     * @param code        The input code (key or mouse button).
     */
    public DigitalBinding(@Nullable InputDevice inputDevice, String action, int code) {
        this(inputDevice, action, code, InputType.KEY);
    }

    /**
     * Constructs a new DigitalBinding.
     *
     * @param inputDevice The device to restrict to, or {@code null} for any.
     * @param action      The action name.
     * @param code        The input code.
     * @param type        The type of input.
     */
    public DigitalBinding(@Nullable InputDevice inputDevice, String action, int code, InputType type) {
        this.device = inputDevice;
        this.action = action;
        this.code = code;
        this.type = type;
    }

    @Override
    public String action() {
        return action;
    }

    @Override
    public boolean matches(InputState state) {
        if (state instanceof GlobalInputState globalState) {
            return switch (type) {
                case KEY -> globalState.isKeyHeld(code);
                case BUTTON -> globalState.isButtonHeld(code);
                default -> false;
            };
        } else if (state instanceof PerDeviceInputState perDeviceState && device != null) {
            return switch (type) {
                case KEY -> perDeviceState.isKeyHeld(device, code);
                case BUTTON -> perDeviceState.isButtonHeld(device, code);
                default -> false;
            };
        }
        return false;
    }

    @Override
    public float value(InputState state) {
        return matches(state) ? 1F : 0F;
    }

    @Override
    public boolean isPressed(InputState state) {
        if (state instanceof GlobalInputState globalState) {
            return switch (type) {
                case KEY -> globalState.isKeyPressed(code);
                case BUTTON -> globalState.isButtonPressed(code);
                default -> false;
            };
        } else if (state instanceof PerDeviceInputState perDeviceState && device != null) {
            return switch (type) {
                case KEY -> perDeviceState.isKeyPressed(device, code);
                case BUTTON -> perDeviceState.isButtonPressed(device, code);
                default -> false;
            };
        }
        return false;
    }

    @Override
    public boolean isReleased(InputState state) {
        if (state instanceof GlobalInputState globalState) {
            return switch (type) {
                case KEY -> globalState.isKeyReleased(code);
                case BUTTON -> globalState.isButtonReleased(code);
                default -> false;
            };
        } else if (state instanceof PerDeviceInputState perDeviceState && device != null) {
            return switch (type) {
                case KEY -> perDeviceState.isKeyReleased(device, code);
                case BUTTON -> perDeviceState.isButtonReleased(device, code);
                default -> false;
            };
        }
        return false;
    }

    @Override
    public boolean isTriggeredBy(InputEvent event) {
        if (device != null && !device.equals(event.device())) {
            return false;
        }
        if (event.code() != code) {
            return false;
        }
        return switch (type) {
            case KEY -> event.type() == InputEventType.KEY_PRESSED || event.type() == InputEventType.KEY_RELEASED;
            case BUTTON -> event.type() == InputEventType.BUTTON_PRESSED || event.type() == InputEventType.BUTTON_RELEASED;
            default -> false;
        };
    }

    @Override
    public float value(InputEvent event) {
        if (isTriggeredBy(event)) {
            return (event.type() == InputEventType.KEY_PRESSED ||
                    event.type() == InputEventType.BUTTON_PRESSED) ? 1F : 0F;
        }
        return 0F;
    }

    @Override
    public int triggerCode() {
        return code;
    }

    @Override
    public InputType type() {
        return type;
    }

    @Override
    public @Nullable InputDevice device() {
        return device;
    }
}
