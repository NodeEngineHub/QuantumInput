package ca.nodeengine.quantum.event;

import ca.nodeengine.quantum.InputDevice;

/**
 * Represents a state change of an input type.
 *
 * @author FX
 */
public interface InputEvent {

    /**
     * Gets the input device which created the event.
     *
     * @return The input device.
     */
    InputDevice device();

    /**
     * Gets the type of this event.
     *
     * @return The event type.
     */
    InputEventType type();

    /**
     * Gets the input code, which can mean different things based on the type.
     * <p>
     * Such as: scancode, button id, etc.
     *
     * @return The input code.
     */
    int code();

    /**
     * Gets the first value for types which support it (e.g., X position, pressure).
     *
     * @return The first value.
     */
    float value1();

    /**
     * Gets the second value for types which support it (e.g., Y position).
     *
     * @return The second value.
     */
    float value2();
}
