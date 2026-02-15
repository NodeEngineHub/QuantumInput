package ca.nodeengine.quantum.api.action;

import ca.nodeengine.quantum.api.InputSystem;
import ca.nodeengine.quantum.api.event.InputEvent;
import ca.nodeengine.quantum.api.event.InputListener;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A collection of {@link ActionBinding}s.
 * <p>
 * An ActionMap defines how multiple actions are mapped to hardware inputs.
 * </p>
 *
 * @author FX
 */
public interface ActionMap {

    /**
     * Gets all bindings associated with a specific action name.
     *
     * @param action The name of the action.
     * @return A list of bindings for the specified action.
     */
    List<ActionBinding> getBindings(String action);

    /**
     * Adds a binding to this action map.
     *
     * @param binding The binding to add.
     */
    void addBinding(ActionBinding binding);

    /**
     * Removes all bindings for a specific action.
     *
     * @param action The name of the action to clear.
     */
    void clearBindings(String action);

    /**
     * Gets all bindings in this map.
     *
     * @return An unmodifiable map of all actions and their bindings.
     */
    Map<String, List<ActionBinding>> getAllBindings();

    /**
     * Adds an action listener to this action map.
     *
     * @param name     The action listener name/id
     * @param listener The action listener
     */
    void addActionListener(String name, ActionListener listener);

    /**
     * Removes an action listener from this action map.
     *
     * @param name The action listener name/id
     */
    void removeActionListener(String name);

    /**
     * Gets a collection of all the action listeners currently registered in this action map.
     *
     * @return The action listeners.
     */
    Collection<ActionListener> getActionListeners();

    /**
     * Creates an {@link InputListener} which binds all the {@link ActionListener}'s
     * in this action map.<br>
     * This should only be called and bound to the {@link InputSystem} once.
     *
     * @return The InputListener which needs to be added.
     */
    default InputListener createInputListener() {
        return event -> {
            for (Map.Entry<String, List<ActionBinding>> entry : getAllBindings().entrySet()) {
                String action = entry.getKey();
                for (ActionBinding binding : entry.getValue()) {
                    if (binding.isTriggeredBy(event)) {
                        for (ActionListener listener : getActionListeners()) {
                            listener.onActionEvent(new DefaultActionEvent(event, action, binding.value(event)));
                        }
                    }
                }
            }
        };
    }

    record DefaultActionEvent(InputEvent event, String action, float value) implements ActionEvent {}
}
