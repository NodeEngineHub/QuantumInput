package ca.nodeengine.quantum.api.event;

/**
 * The type of the event.
 *
 * @author FX
 */
public enum InputEventType {
    KEY_PRESSED,
    KEY_RELEASED,
    // TODO: Not currently implemented as different operating systems do it differently.
    //  May want to do it all within Quantum.
    KEY_REPEAT,
    BUTTON_PRESSED,  // mouse
    BUTTON_RELEASED, // mouse
    AXIS_CHANGED,
    MOUSE_CHANGED,
    SCROLL
}
