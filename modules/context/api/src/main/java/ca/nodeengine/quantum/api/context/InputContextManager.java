package ca.nodeengine.quantum.api.context;

import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.action.ActionMap;
import ca.nodeengine.quantum.api.state.InputState;

import java.util.List;

/**
 * Context Stack / Resolver
 * Determines which context “wins”.
 *
 * @see ContextRules
 */
public interface InputContextManager {
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

    List<InputContext> getActiveContexts();

    /**
     * Checks if an action is currently active.
     * <p>
     * For digital actions, this usually means the button is held.
     * For analog actions, this usually means the value is above a certain threshold.
     * </p>
     *
     * @param action The name of the action to check.
     * @return {@code true} if the action is active, otherwise {@code false}.
     */
    default boolean isActive(InputState state, String action) {
        return getValue(state, action) > 0.5F;
    }

    /**
     * Gets the current value of an action.
     * <p>
     * For digital actions, this is usually {@code 0.0} or {@code 1.0}.
     * For analog actions, this returns the continuous value of the bound axis.
     * </p>
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
