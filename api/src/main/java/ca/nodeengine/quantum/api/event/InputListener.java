package ca.nodeengine.quantum.api.event;

/**
 * Implement this interface to receive inputs using an event-based approach.
 *
 * @author FX
 */
@FunctionalInterface
public interface InputListener {

    /**
     * Called when an input event occurs.
     *
     * @param event The input event.
     */
    void onInputEvent(InputEvent event);
}
