package ca.nodeengine.quantum.api.action;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.state.InputState;
import org.jspecify.annotations.Nullable;

/**
 * Move away from keys to intent.
 */
public interface ActionBinding {
    @Nullable InputDevice device();
    String action();
    boolean matches(InputState state);
    float value(InputState state); // digital = 0/1, analog allowed
}
