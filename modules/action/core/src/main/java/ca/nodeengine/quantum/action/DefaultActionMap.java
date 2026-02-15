package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.action.ActionMap;

import java.util.*;

public class DefaultActionMap implements ActionMap {
    private final Map<String, List<ActionBinding>> bindings = new HashMap<>();

    public void addBinding(ActionBinding binding) {
        String action = binding.action();
        bindings.computeIfAbsent(action, _ -> new ArrayList<>()).add(binding);
    }

    @Override
    public List<ActionBinding> getBindings(String action) {
        return bindings.getOrDefault(action, Collections.emptyList());
    }

    public Map<String, List<ActionBinding>> getAllBindings() {
        return Collections.unmodifiableMap(bindings);
    }
}
