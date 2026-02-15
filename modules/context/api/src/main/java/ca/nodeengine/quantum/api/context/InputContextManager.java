package ca.nodeengine.quantum.api.context;

import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.action.ActionMap;
import ca.nodeengine.quantum.api.state.InputState;

import java.util.List;

/**
 * Context Stack / Resolver<p>
 * Determines which context “wins”.<br>
 * The manager handles all contexts and how they are used.
 *
 * @see ContextRules
 * @author FX
 */
public interface InputContextManager {

    /**
     * Activates a registered context.
     *
     * @param context The context to activate.
     */
    void pushContext(InputContext context);

    /**
     * Activates a registered context by name.
     *
     * @param name The name of the context to activate.
     */
    void pushContext(String name);

    /**
     * Deactivates an active context by name.
     *
     * @param name The name of the context to deactivate.
     */
    void popContext(String name);

    /**
     * Gets all the active contexts.
     *
     * @return The active contexts.
     */
    List<InputContext> getActiveContexts();

    /**
     * Checks if an action is currently active.
     * <p>
     * For digital actions, this usually means the button is held.
     * For analog actions, this usually means the value is above the activation threshold.
     *
     * @param action The name of the action to check.
     * @return {@code true} if the action is active, otherwise {@code false}.
     */
    default boolean isActive(InputState state, String action) {
        return getValue(state, action) > getActivationThreshold();
    }

    /**
     * Gets the activation threshold for analog actions.
     *
     * @return The activation threshold.
     */
    default float getActivationThreshold() {
        return 0.5F;
    }

    /**
     * Sets the activation threshold for analog actions.
     *
     * @param threshold The new activation threshold.
     */
    default void setActivationThreshold(float threshold) {
    }

    /**
     * Checks if an action was just pressed/activated this frame.
     *
     * @param state  The current input state.
     * @param action The name of the action.
     * @return {@code true} if just pressed.
     */
    default boolean isPressed(InputState state, String action) {
        List<InputContext> activeContexts = getActiveContexts();
        for (InputContext context : activeContexts) {
            ActionMap actionMap = context.actionMap();
            List<ActionBinding> bindings = actionMap.getBindings(action);
            if (!bindings.isEmpty()) {
                for (ActionBinding binding : bindings) {
                    if (binding.isPressed(state)) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    /**
     * Checks if an action was just released/deactivated this frame.
     *
     * @param state  The current input state.
     * @param action The name of the action.
     * @return {@code true} if just released.
     */
    default boolean isReleased(InputState state, String action) {
        List<InputContext> activeContexts = getActiveContexts();
        for (InputContext context : activeContexts) {
            ActionMap actionMap = context.actionMap();
            List<ActionBinding> bindings = actionMap.getBindings(action);
            if (!bindings.isEmpty()) {
                for (ActionBinding binding : bindings) {
                    if (binding.isReleased(state)) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    /**
     * Gets the current value of an action.
     * <p>
     * For digital actions, this is usually {@code 0.0} or {@code 1.0}.
     * For analog actions, this returns the continuous value of the bound axis.
     *
     * @param action The name of the action to query.
     * @return The current value of the action.
     */
    default float getValue(InputState state, String action) {
        List<InputContext> activeContexts = getActiveContexts();
        for (InputContext context : activeContexts) {
            ActionMap actionMap = context.actionMap();
            List<ActionBinding> bindings = actionMap.getBindings(action);
            if (!bindings.isEmpty()) {
                float max = 0F;
                for (ActionBinding binding : bindings) {
                    max = Math.max(max, binding.value(state));
                }
                return max;
            }
        }
        return 0F;
    }
}
