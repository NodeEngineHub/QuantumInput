package ca.nodeengine.quantum.api.state;

/**
 * The input state is a queryable state of all the inputs.
 * <p>
 * Most implementations are frame-coherent state caches.
 * </p>
 *
 * @author FX
 */
public interface InputState {

    /**
     * Updates the input states.
     * <p>
     * If this is a frame-coherent system, this should be called at the beginning of a frame.
     * This should be done before setting or polling states from the platform.
     * </p>
     */
    void update();

    /**
     * Resets all input states.
     * <p>
     * WARNING: This can cause issues if the user is currently pressing a key,
     * as it may miss the release event.
     * </p>
     */
    void reset();
}
