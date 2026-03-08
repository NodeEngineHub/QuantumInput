package ca.nodeengine.quantum;

import ca.nodeengine.quantum.event.InputEvent;
import ca.nodeengine.quantum.state.InputState;
import ca.nodeengine.quantum.state.MutableInputState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Default implementation of {@link InputProcessor}.
 * <p>
 * This processor handles both global and per-device input states by delegating
 * events to the underlying {@link MutableInputState}.
 *
 * @param <IS> The input state type.
 * @author FX
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class DefaultInputProcessor<IS extends InputState & MutableInputState> implements InputProcessor<IS> {

    /** The input state managed by this processor. */
    private final IS state;

    @Override
    public void onInputEvent(InputEvent event) {
        switch (event.type()) {
            case KEY_PRESSED -> state.setKey(event.device(), event.code(), true);
            case KEY_RELEASED -> state.setKey(event.device(), event.code(), false);
            case BUTTON_PRESSED -> state.setButton(event.device(), event.code(), true);
            case BUTTON_RELEASED -> state.setButton(event.device(), event.code(), false);
            case AXIS_CHANGED -> state.setAxis(event.device(), event.code(), event.value1());
            case MOUSE_CHANGED -> state.setMousePos(event.device(), event.value1(), event.value2());
            case SCROLL -> state.setScroll(event.device(), event.value1(), event.value2());
            default -> throw new IllegalStateException("Type `" + event.type() + "` is currently not supported!");
        }
    }
}
