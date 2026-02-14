package ca.nodeengine.quantum.api.event;

/**
 * Implement this interface to receive inputs using an Event based approach.
 *
 * @author FX
 */
public interface InputListener {

    /**
     * This is called when any input event is made.
     *
     * @param event The input event
     */
    void onInputEvent(InputEvent event);
}
