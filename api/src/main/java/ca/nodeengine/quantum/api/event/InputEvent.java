package ca.nodeengine.quantum.api.event;

import ca.nodeengine.quantum.api.InputDevice;

/**
 * Represent a state change of an input type.
 *
 * @author FX
 */
public interface InputEvent {

    /**
     * The input device which created the event.
     *
     * @return The input device.
     */
    InputDevice device();

    /**
     * Get the type of this event.
     *
     * @return The event type
     */
    InputEventType type();

    /**
     * The input code, which can mean different things based on the type.<br>
     * Such as: scancode, button id, etc.
     *
     * @return The input code.
     */
    int code();

    /**
     * The first value for types which support it.
     *
     * @return The first value
     */
    float value1();

    /**
     * The second value for types which support it.
     *
     * @return The second value
     */
    float value2();
}
