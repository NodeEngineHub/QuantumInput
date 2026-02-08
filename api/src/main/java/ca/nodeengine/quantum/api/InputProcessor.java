package ca.nodeengine.quantum.api;

/**
 * This connects the event-driven core to the state cache. (Event -> State)
 * A typical implementation:
 *  - Receives events
 *  - Mutates internal state
 *  - Exposes snapshot queries via InputState
 *
 * @author FX
 */
public interface InputProcessor extends InputEventListener {
    InputState state();
}
