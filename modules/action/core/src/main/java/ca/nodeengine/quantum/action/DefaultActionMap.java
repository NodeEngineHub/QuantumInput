package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.InputType;
import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.action.ActionListener;
import ca.nodeengine.quantum.api.action.ActionMap;
import ca.nodeengine.quantum.api.event.InputEvent;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.BooleanSupplier;

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
    /** Lookup map for efficient event dispatching: code -> bindings */
    private final Map<Integer, List<ActionBinding>> bindingsByCode = new HashMap<>();
    /** Internal list of all the action listeners */
    private final Map<String, ActionListener> listeners = new HashMap<>(0);
    /** Predicate to determine if this action map is active */
    private @Nullable BooleanSupplier activePredicate = null;

    @Override
    public ActionMap add(String actionName, int code) {
        return add(actionName, null, code);
    }

    @Override
    public ActionMap add(String actionName, @Nullable InputDevice device, int code) {
        return add(actionName, device, code, InputType.KEY);
    }

    @Override
    public ActionMap add(String actionName, @Nullable InputDevice device, int code, InputType type) {
        if (type == InputType.AXIS) {
            return add(new AxisBinding(device, actionName, code));
        }
        return add(new DigitalBinding(device, actionName, code, type));
    }

    @Override
    public ActionMap add(ActionBinding binding) {
        String action = binding.action();
        bindings.computeIfAbsent(action, _ -> new ArrayList<>()).add(binding);

        int code = binding.triggerCode();
        if (code != -1) {
            bindingsByCode.computeIfAbsent(code, _ -> new ArrayList<>()).add(binding);
        } else {
            // Bindings with no specific code (like composites) are stored with code -1
            bindingsByCode.computeIfAbsent(-1, _ -> new ArrayList<>()).add(binding);
        }
        return this;
    }

    @Override
    public ActionBinding createBinding(String actionName, int code) {
        return createBinding(actionName, null, code);
    }

    @Override
    public ActionBinding createBinding(String actionName, @Nullable InputDevice device, int code) {
        return createBinding(actionName, device, code, InputType.KEY);
    }

    @Override
    public ActionBinding createBinding(String actionName, @Nullable InputDevice device, int code, InputType type) {
        if (type == InputType.AXIS) {
            return new AxisBinding(device, actionName, code);
        }
        return new DigitalBinding(device, actionName, code, type);
    }

    @Override
    public ActionBinding createAxisBinding(String actionName, @Nullable InputDevice device, int axisCode,
                                           float deadzone, float scale) {
        return new AxisBinding(device, actionName, axisCode, deadzone, scale);
    }

    @Override
    public ActionBinding createCompositeBinding(String actionName, ActionBinding... components) {
        return new CompositeBinding(actionName, components);
    }

    @Override
    public List<ActionBinding> getBindings(String action) {
        return bindings.getOrDefault(action, Collections.emptyList());
    }

    @Override
    public void clearBindings(String action) {
        List<ActionBinding> removed = bindings.remove(action);
        if (removed != null) {
            for (ActionBinding binding : removed) {
                int code = binding.triggerCode();
                List<ActionBinding> byCode = bindingsByCode.get(code);
                if (byCode != null) {
                    byCode.remove(binding);
                }
            }
        }
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

    @Override
    public boolean isActive() {
        return activePredicate == null || activePredicate.getAsBoolean();
    }

    @Override
    public void setActivePredicate(@Nullable BooleanSupplier activePredicate) {
        this.activePredicate = activePredicate;
    }

    @Override
    public ca.nodeengine.quantum.api.event.InputListener createInputListener() {
        return event -> {
            if (!isActive()) {
                return;
            }

            // Check bindings specifically for this code
            processBindings(bindingsByCode.get(event.code()), event);
            // Check bindings that don't have a specific code (composites)
            processBindings(bindingsByCode.get(-1), event);
        };
    }

    private void processBindings(@Nullable List<ActionBinding> bindings, InputEvent event) {
        if (bindings == null) {
            return;
        }
        for (ActionBinding binding : bindings) {
            if (binding.isTriggeredBy(event)) {
                String action = binding.action();
                float value = binding.value(event);
                for (ActionListener listener : getActionListeners()) {
                    listener.onActionEvent(new ActionMap.DefaultActionEvent(event, action, value));
                }
            }
        }
    }
}
