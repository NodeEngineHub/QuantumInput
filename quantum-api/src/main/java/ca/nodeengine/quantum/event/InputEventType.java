package ca.nodeengine.quantum.event;

/**
 * The type of the input event.
 *
 * @author FX
 */
public enum InputEventType {
    /**
     * A keyboard key was pressed.
     */
    KEY_PRESSED,

    /**
     * A keyboard key was released.
     */
    KEY_RELEASED,

    /**
     * A keyboard key was repeated (auto-repeat).
     * <p>
     * NOTE: Not currently implemented as different operating systems do it differently.
     * May want to do it all within QuantumInput in the future.
     */
    KEY_REPEAT,

    /**
     * A mouse button was pressed.
     */
    BUTTON_PRESSED,

    /**
     * A mouse button was released.
     */
    BUTTON_RELEASED,

    /**
     * An analog axis value has changed.
     */
    AXIS_CHANGED,

    /**
     * The mouse position has changed.
     */
    MOUSE_CHANGED,

    /**
     * The mouse scroll wheel was moved.
     */
    SCROLL
}
