package ca.nodeengine.quantum.api;

import ca.nodeengine.quantum.api.event.InputListener;
import ca.nodeengine.quantum.api.state.InputState;

/**
 * Receives raw platform events and updates the internal state.
 * <p>
 * This connects the event-driven core to the state cache. (Event -&gt; State)
 * A typical implementation:
 * <ul>
 *  <li>Receives events</li>
 *  <li>Mutates internal {@link InputState} with new values</li>
 * </ul>
 *
 * @param <IS> The type of the input state.
 * @author FX
 */
public interface InputProcessor<IS extends InputState> extends InputListener {

    /**
     * Gets the internal input state of the processor.
     *
     * @return The input state.
     */
    IS state();
}
