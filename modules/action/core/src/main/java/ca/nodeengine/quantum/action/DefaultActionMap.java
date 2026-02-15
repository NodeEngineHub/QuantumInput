package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.action.ActionListener;
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
    /** Internal list of all the action listeners */
    private final Map<String, ActionListener> listeners = new HashMap<>(0);

    @Override
    public void addBinding(ActionBinding binding) {
        String action = binding.action();
        bindings.computeIfAbsent(action, _ -> new ArrayList<>()).add(binding);
    }

    @Override
    public List<ActionBinding> getBindings(String action) {
        return bindings.getOrDefault(action, Collections.emptyList());
    }

    @Override
    public void clearBindings(String action) {
        bindings.remove(action);
    }

    @Override
    public Map<String, List<ActionBinding>> getAllBindings() {
        return Collections.unmodifiableMap(bindings);
    }

    @Override
    public void addActionListener(String name, ActionListener listener) {
        listeners.put(name, listener);
    }

    @Override
    public void removeActionListener(String name) {
        listeners.remove(name);
    }

    @Override
    public Collection<ActionListener> getActionListeners() {
        return listeners.values();
    }
}
