package ca.nodeengine.quantum.event;

import ca.nodeengine.quantum.api.InputCode;
import ca.nodeengine.quantum.api.InputEvent;

public record AxisChangedEvent(InputCode axis, float value, long timestamp) implements InputEvent {
}
