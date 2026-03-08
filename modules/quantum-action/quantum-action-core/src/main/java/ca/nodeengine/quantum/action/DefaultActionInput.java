package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.state.InputState;

import java.util.List;

/**
 * Default implementation of {@link ActionInput}.
 * <p>
 * This implementation resolves action states by checking all bindings in an {@link ActionMap}
 * against a provided {@link InputState}.
 * </p>
 *
 * @author FX
 */
public class DefaultActionInput implements ActionInput {

    /** The input state to query. */
    private final InputState state;
    /** The action map containing the bindings. */
    private final ActionMap actionMap;

    /**
     * Constructs a new DefaultActionInput.
     *
     * @param state     The input state.
     * @param actionMap The action map.
     */
    public DefaultActionInput(InputState state, ActionMap actionMap) {
        this.state = state;
        this.actionMap = actionMap;
    }

    @Override
    public boolean isActive(String action) {
        return getValue(action) > 0.5F;
    }

    @Override
    public float getValue(String action) {
        List<ActionBinding> bindings = actionMap.getBindings(action);
        if (!bindings.isEmpty()) {
            float max = 0;
            for (ActionBinding binding : bindings) {
                max = Math.max(max, binding.value(state));
            }
            return max;
        }
        return 0.0f;
    }
}
