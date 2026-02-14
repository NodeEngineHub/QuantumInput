package ca.nodeengine.quantum.api;

import ca.nodeengine.quantum.api.event.InputListener;
import ca.nodeengine.quantum.api.state.InputState;

/**
 * This connects the event-driven core to the state cache. (Event -> State)
 * A typical implementation:
 *  - Receives events
 *  - Mutates internal InputState with new values
 *
 * @author FX
 */
public interface InputProcessor<IS extends InputState> extends InputListener {

    /**
     * The internal input state of the processor.
     *
     * @return The input state
     */
    IS state();
}
