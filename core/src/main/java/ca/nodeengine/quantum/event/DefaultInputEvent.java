package ca.nodeengine.quantum.event;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.event.InputEvent;
import ca.nodeengine.quantum.api.event.InputEventType;

/**
 * The default implementation of {@link InputEvent}.
 *
 * @author FX
 */
public record DefaultInputEvent(InputDevice device, InputEventType type, int code, float value1, float value2)
        implements InputEvent {
}
