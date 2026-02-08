package ca.nodeengine.quantum;

import ca.nodeengine.quantum.api.InputEvent;
import ca.nodeengine.quantum.api.InputProcessor;
import ca.nodeengine.quantum.api.MutableInputState;
import ca.nodeengine.quantum.event.AxisChangedEvent;
import ca.nodeengine.quantum.event.KeyPressedEvent;
import ca.nodeengine.quantum.event.KeyReleasedEvent;

public record DefaultInputProcessor(MutableInputState state) implements InputProcessor {

    @Override
    public void onInputEvent(InputEvent event) {
        if (event instanceof KeyPressedEvent keyPressedEvent) {
            state.setDown(keyPressedEvent.key(), true);
        } else if (event instanceof KeyReleasedEvent keyReleasedEvent) {
            state.setDown(keyReleasedEvent.key(), false);
        } else if (event instanceof AxisChangedEvent axisChangedEvent) {
            state.setAxis(axisChangedEvent.axis(), axisChangedEvent.value());
        }
    }
}
