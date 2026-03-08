package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.InputDevice;
import ca.nodeengine.quantum.InputType;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A builder for creating and adding bindings to an {@link ActionMap}.
 *
 * @author FX
 */
public final class BindingBuilder {
    private final ActionMap actionMap;
    private final String actionName;
    private final List<Integer> modifiers = new ArrayList<>();
    private @Nullable InputDevice device;

    private float deadzone = -1;
    private float scale = 1;

    public BindingBuilder(ActionMap actionMap, String actionName) {
        this.actionMap = actionMap;
        this.actionName = actionName;
    }

    public BindingBuilder device(InputDevice device) {
        this.device = device;
        return this;
    }

    public BindingBuilder with(int modifierCode) {
        modifiers.add(modifierCode);
        return this;
    }

    public BindingBuilder deadzone(float deadzone) {
        this.deadzone = deadzone;
        return this;
    }

    public BindingBuilder scale(float scale) {
        this.scale = scale;
        return this;
    }

    private ActionMap addBinding(ActionBinding binding) {
        if (!modifiers.isEmpty()) {
            ActionBinding[] components = new ActionBinding[modifiers.size() + 1];
            for (int i = 0; i < modifiers.size(); i++) {
                components[i] = actionMap.createBinding(actionName, device, modifiers.get(i), InputType.KEY);
            }
            components[modifiers.size()] = binding;
            binding = actionMap.createCompositeBinding(actionName, components);
        }
        return actionMap.add(binding);
    }

    public ActionMap toKey(int keyCode) {
        return addBinding(actionMap.createBinding(actionName, device, keyCode, InputType.KEY));
    }

    public ActionMap toButton(int buttonCode) {
        return addBinding(actionMap.createBinding(actionName, device, buttonCode, InputType.BUTTON));
    }

    public ActionMap toAxis(int axisCode) {
        if (deadzone != -1 || scale != 1) {
            return addBinding(actionMap.createAxisBinding(actionName, device, axisCode,
                    deadzone == -1 ? 0.05F : deadzone, scale));
        }
        return addBinding(actionMap.createBinding(actionName, device, axisCode, InputType.AXIS));
    }

    public ActionMap or(ActionBinding... bindings) {
        return actionMap.add(actionMap.createOrBinding(actionName, bindings));
    }

    /**
     * @deprecated Use {@link #toKey(int)} or {@link #toButton(int)} for clarity.
     */
    @SuppressWarnings("InlineMeSuggester")
    @Deprecated
    public ActionMap to(int code) {
        return toKey(code);
    }
}
