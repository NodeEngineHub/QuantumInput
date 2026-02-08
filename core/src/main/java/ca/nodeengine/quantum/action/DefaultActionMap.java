package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.action.ActionMap;
import ca.nodeengine.quantum.api.action.InputAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultActionMap implements ActionMap {
    private final Map<String, InputAction> actionsByName = new HashMap<>();
    private final Map<InputAction, List<ActionBinding>> bindings = new HashMap<>();

    public void addBinding(ActionBinding binding) {
        InputAction action = binding.action();
        actionsByName.putIfAbsent(action.name(), action);
        bindings.computeIfAbsent(action, k -> new ArrayList<>()).add(binding);
    }

    @Override
    public InputAction getAction(String name) {
        InputAction action = actionsByName.get(name);
        if (action == null) {
            throw new IllegalArgumentException("Action not found: " + name);
        }
        return action;
    }

    @Override
    public List<ActionBinding> getBindings(InputAction action) {
        return bindings.getOrDefault(action, Collections.emptyList());
    }

    public Map<InputAction, List<ActionBinding>> getAllBindings() {
        return Collections.unmodifiableMap(bindings);
    }
}
