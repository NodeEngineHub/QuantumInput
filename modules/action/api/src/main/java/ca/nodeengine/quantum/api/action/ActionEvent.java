package ca.nodeengine.quantum.api.action;

import ca.nodeengine.quantum.api.event.InputEvent;

/**
 * Represents a state change of an action.
 *
 * @author FX
 */
public interface ActionEvent {

    /**
     * Gets the input event which triggered this action event.
     *
     * @return The event.
     */
    InputEvent event();

    /**
     * Gets the name of the action that triggered the event.
     *
     * @return The action name.
     */
    String action();

    /**
     * Gets the current value of the action.
     *
     * @return The action value.
     */
    float value();

    /**
     * Checks if the action is considered active.
     *
     * @return {@code true} if active, otherwise {@code false}.
     */
    default boolean isActive() {
        return value() > 0.5f;
    }
}
