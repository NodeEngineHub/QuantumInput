package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.action.ActionMap;
import ca.nodeengine.quantum.api.action.ActionMapBuilder;
import org.jspecify.annotations.Nullable;

public class DefaultActionMapBuilder implements ActionMapBuilder {
    private final DefaultActionMap actionMap = new DefaultActionMap();

    @Override
    public ActionMapBuilder add(String actionName, int code) {
        return add(actionName, null, code);
    }

    @Override
    public ActionMapBuilder add(String actionName, @Nullable InputDevice device, int code) {
        actionMap.addBinding(new DigitalBinding(device, actionName, code));
        return this;
    }

    @Override
    public ActionMap build() {
        return actionMap;
    }
}
