package ca.nodeengine.quantum.event;

import ca.nodeengine.quantum.api.InputCode;
import ca.nodeengine.quantum.api.InputEvent;

public record KeyPressedEvent(InputCode key, long timestamp) implements InputEvent {
}
