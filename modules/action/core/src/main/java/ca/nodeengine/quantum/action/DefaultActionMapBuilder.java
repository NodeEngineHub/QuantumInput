package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.action.ActionMap;
import ca.nodeengine.quantum.api.action.ActionMapBuilder;
import ca.nodeengine.quantum.api.action.InputAction;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class DefaultActionMapBuilder implements ActionMapBuilder {
    private final DefaultActionMap actionMap = new DefaultActionMap();
    private final Map<String, InputAction> actions = new HashMap<>();

    @Override
    public ActionMapBuilder add(String actionName, int code) {
        return add(actionName, null, code);
    }

    @Override
    public ActionMapBuilder add(String actionName, @Nullable InputDevice device, int code) {
        InputAction action = actions.computeIfAbsent(actionName, DefaultInputAction::new);
        actionMap.addBinding(new DigitalBinding(device, action, code));
        return this;
    }

    @Override
    public ActionMap build() {
        return actionMap;
    }
}
