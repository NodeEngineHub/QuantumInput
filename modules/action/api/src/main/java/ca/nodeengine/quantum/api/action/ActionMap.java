package ca.nodeengine.quantum.api.action;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.InputType;
import ca.nodeengine.quantum.api.InputSystem;
import ca.nodeengine.quantum.api.event.InputEvent;
import ca.nodeengine.quantum.api.event.InputListener;
import org.jspecify.annotations.Nullable;

import org.jetbrains.annotations.Contract;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * A collection of {@link ActionBinding}s.
 * <p>
 * An ActionMap defines how multiple actions are mapped to hardware inputs.
 * </p>
 *
 * @author FX
 */
public interface ActionMap {

    //region Bindings

    /**
     * Gets all bindings associated with a specific action name.
     *
     * @param action The name of the action.
     * @return A list of bindings for the specified action.
     */
    List<ActionBinding> getBindings(String action);

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
    //endregion

    //region Add Bindings

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
     * Adds a digital binding of a specific type to the action map.
     *
     * @param actionName The name of the action.
     * @param device The input device, or {@code null} for global.
     * @param code The input code.
     * @param type The input type.
     * @return This action map.
     */
    @Contract("_,_,_,_->this")
    ActionMap add(String actionName, @Nullable InputDevice device, int code, InputType type);

    /**
     * Fluent binding builder.
     *
     * @param actionName The name of the action.
     * @return A binding builder.
     */
    default BindingBuilder bind(String actionName) {
        return new BindingBuilder(this, actionName);
    }

    /**
     * Adds a binding to this action map.
     *
     * @param binding The binding to add.
     * @return This action map.
     */
    @Contract("_->this")
    ActionMap add(ActionBinding binding);
    //endregion

    //region Create Digital Bindings

    /**
     * Creates a digital binding.
     *
     * @param actionName The name of the action.
     * @param code The input code (key or button).
     * @return The new binding
     */
    @Contract("_,_->new")
    ActionBinding createBinding(String actionName, int code);

    /**
     * Creates a digital binding.
     *
     * @param actionName The name of the action.
     * @param device The input device, or {@code null} for global.
     * @param code The input code (key or button).
     * @return The new binding
     */
    @Contract("_,_,_->new")
    ActionBinding createBinding(String actionName, @Nullable InputDevice device, int code);

    /**
     * Creates a digital binding.
     *
     * @param actionName The name of the action.
     * @param device The input device, or {@code null} for global.
     * @param code The input code (key or button).
     * @param type The input type.
     * @return The new binding
     */
    @Contract("_,_,_,_->new")
    ActionBinding createBinding(String actionName, @Nullable InputDevice device, int code, InputType type);

    /**
     * Creates an axis binding.
     *
     * @param actionName The name of the action.
     * @param device     The input device, or {@code null} for global.
     * @param axisCode   The axis code.
     * @param deadzone   The deadzone.
     * @param scale      The scale.
     * @return The new binding
     */
    @Contract("_,_,_,_,_->new")
    default ActionBinding createAxisBinding(String actionName, @Nullable InputDevice device, int axisCode,
                                            float deadzone, float scale) {
        return createBinding(actionName, device, axisCode, InputType.AXIS);
    }
    //endregion

    //region Create Composite Bindings

    /**
     * Creates a composite binding, which is a grouping of multiple bindings together.
     *
     * @param actionName The name of the action.
     * @param components The action bindings making up this composite binding
     * @return The new composite binding
     */
    @Contract("_,_->new")
    ActionBinding createCompositeBinding(String actionName, ActionBinding... components);
    //endregion

    //region Listeners

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
            if (!isActive()) {
                return;
            }
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
    //endregion

    //region Active State

    /**
     * Checks if this action map is currently active.
     * <p>
     * If no predicate is set, this returns {@code true} by default.
     * </p>
     *
     * @return {@code true} if active, otherwise {@code false}.
     */
    default boolean isActive() {
        return true;
    }

    /**
     * Sets a predicate to determine if this action map is active.
     *
     * @param activePredicate The predicate, or {@code null} to reset to default.
     */
    default void setActivePredicate(@Nullable BooleanSupplier activePredicate) {
    }
    //endregion

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
