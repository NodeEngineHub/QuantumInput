package ca.nodeengine.quantum.event;

import ca.nodeengine.quantum.InputDevice;

/**
 * The default implementation of {@link InputEvent}.
 *
 * @param device The input device which created the event.
 * @param type   The type of the event.
 * @param code   The input code (scancode, button id, etc.).
 * @param value1 The first value for types which support it.
 * @param value2 The second value for types which support it.
 * @author FX
 */
public record DefaultInputEvent(InputDevice device, InputEventType type, int code, float value1, float value2)
        implements InputEvent {
}
