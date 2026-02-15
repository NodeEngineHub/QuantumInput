package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.action.ActionMap;
import ca.nodeengine.quantum.api.action.ActionMapBuilder;
import org.jspecify.annotations.Nullable;

/**
 * Default implementation of {@link ActionMapBuilder}.
 *
 * @author FX
 */
public class DefaultActionMapBuilder implements ActionMapBuilder {

    /** The action map being built. */
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
