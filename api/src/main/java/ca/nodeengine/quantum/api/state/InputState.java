package ca.nodeengine.quantum.api.state;

/**
 * The input state is a queryable state of all the inputs.<br>
 * Most implementations are Frame-Coherent state caches.
 *
 * @author FX
 */
public interface InputState {

    /**
     * Called to update the input states.<br>
     * If this is a Frame-Coherent system, this should be called at the beginning of a frame.<br>
     * This should be done before setting or polling states from the platform
     */
    void update();

    /**
     * Reset all states.<br>
     * This can cause issues if the user is currently pressing on a key.
     */
    void reset();
}
