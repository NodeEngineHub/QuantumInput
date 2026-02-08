package ca.nodeengine.quantum.api.action;

import ca.nodeengine.quantum.api.InputState;

/**
 * Move away from keys to intent.
 */
public interface ActionBinding {
    InputAction action();
    boolean matches(InputState state);
    float value(InputState state); // digital = 0/1, analog allowed
}
