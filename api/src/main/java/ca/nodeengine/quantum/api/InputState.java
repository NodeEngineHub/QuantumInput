package ca.nodeengine.quantum.api;

/**
 * This is where events become queryable state. State Cache (Frame-Coherent Input State)
 * `beginFrame()` matters because it lets you:
 * - Clear per-frame flags (pressed, released)
 * - Support fixed timestep updates cleanly
 */
public interface InputState {
    boolean isDown(InputCode code);

    float getAxis(InputCode axis);

    void poll();
}
