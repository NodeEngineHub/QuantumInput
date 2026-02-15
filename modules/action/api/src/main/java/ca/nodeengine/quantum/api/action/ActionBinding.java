package ca.nodeengine.quantum.api.action;

import ca.nodeengine.quantum.api.state.InputState;

/**
 * Maps a high-level intent (action) to a specific hardware input.
 * <p>
 * This allows the application to query for "Jump" or "Fire" without
 * caring which specific key or button is bound to it.
 * </p>
 *
 * @author FX
 */
public interface ActionBinding {

    /**
     * Gets the name of the action this binding is for.
     *
     * @return The action name.
     */
    String action();

    /**
     * Checks if this binding is currently active (e.g., button is pressed).
     *
     * @param state The current input state to check against.
     * @return {@code true} if the binding matches, otherwise {@code false}.
     */
    boolean matches(InputState state);

    /**
     * Gets the current value of this binding.
     * <p>
     * For digital bindings, this will be {@code 0.0} or {@code 1.0}.
     * For analog bindings, this will be the axis value.
     * </p>
     *
     * @param state The current input state to check against.
     * @return The binding's value.
     */
    float value(InputState state);
}
