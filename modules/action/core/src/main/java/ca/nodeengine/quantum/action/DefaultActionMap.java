package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.action.ActionMap;

import java.util.*;

/**
 * Default implementation of {@link ActionMap}.
 * <p>
 * This implementation stores bindings in a hash map for efficient retrieval.
 * </p>
 *
 * @author FX
 */
public class DefaultActionMap implements ActionMap {

    /** Internal map of action names to their bindings. */
    private final Map<String, List<ActionBinding>> bindings = new HashMap<>();

    /**
     * Adds a binding to this action map.
     *
     * @param binding The binding to add.
     */
    public void addBinding(ActionBinding binding) {
        String action = binding.action();
        bindings.computeIfAbsent(action, _ -> new ArrayList<>()).add(binding);
    }

    @Override
    public List<ActionBinding> getBindings(String action) {
        return bindings.getOrDefault(action, Collections.emptyList());
    }

    /**
     * Gets all bindings in this map.
     *
     * @return An unmodifiable map of all actions and their bindings.
     */
    public Map<String, List<ActionBinding>> getAllBindings() {
        return Collections.unmodifiableMap(bindings);
    }
}
