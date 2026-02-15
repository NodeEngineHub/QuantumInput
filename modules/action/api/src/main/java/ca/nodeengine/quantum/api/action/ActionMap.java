package ca.nodeengine.quantum.api.action;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.InputSystem;
import ca.nodeengine.quantum.api.event.InputEvent;
import ca.nodeengine.quantum.api.event.InputListener;
import org.jspecify.annotations.Nullable;

import org.jetbrains.annotations.Contract;

import java.util.*;

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
     * Adds a digital binding to the action map.
     *
     * @param actionName The name of the action.
     * @param code The input code (key or button).
     * @return This action map.
     */
    @Contract("_,_->this")
    ActionMap add(String actionName, int code);

    /**
     * Adds a digital binding for a specific device to the action map.
     *
     * @param actionName The name of the action.
     * @param device The input device, or {@code null} for global.
     * @param code The input code (key or button).
     * @return This action map.
     */
    @Contract("_,_,_->this")
    ActionMap add(String actionName, @Nullable InputDevice device, int code);

    /**
     * Adds a binding to this action map.
     *
     * @param binding The binding to add.
     * @return This action map.
     */
    @Contract("_->this")
    ActionMap add(ActionBinding binding);

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

    /**
     * Creates a new instance of an ActionMap.
     *
     * @return A new ActionMap instance.
     */
    static ActionMap create() {
        return ServiceLoader.load(ActionMap.class)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No ActionMap implementation found"));
    }

    record DefaultActionEvent(InputEvent event, String action, float value) implements ActionEvent {}
}
