package ca.nodeengine.quantum.context;

import ca.nodeengine.quantum.api.action.ActionMap;
import ca.nodeengine.quantum.api.context.InputContext;

public final class DefaultInputContext implements InputContext {
    private final String name;
    private final ActionMap actionMap;
    private final int priority;

    public DefaultInputContext(String name, ActionMap actionMap, int priority) {
        this.name = name;
        this.actionMap = actionMap;
        this.priority = priority;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ActionMap actionMap() {
        return actionMap;
    }

    @Override
    public int priority() {
        return priority;
    }
}
