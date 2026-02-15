package ca.nodeengine.quantum.api.action;

/**
 * Public-facing API for querying action states.
 * <p>
 * This is the primary interface used by gameplay code to check for player input.
 *
 * @author FX
 */
public interface ActionInput {

    /**
     * Checks if an action is currently active.
     * <p>
     * For digital actions, this usually means the button is held.
     * For analog actions, this usually means the value is above a certain threshold.
     *
     * @param action The name of the action to check.
     * @return {@code true} if the action is active, otherwise {@code false}.
     */
    boolean isActive(String action);

    /**
     * Gets the current value of an action.
     * <p>
     * For digital actions, this is usually {@code 0.0} or {@code 1.0}.
     * For analog actions, this returns the continuous value of the bound axis.
     *
     * @param action The name of the action to query.
     * @return The current value of the action.
     */
    float getValue(String action);
}
