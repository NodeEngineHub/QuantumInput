package ca.nodeengine.quantum.api.context;

import ca.nodeengine.quantum.api.action.ActionMap;

/**
 * Contexts allow layered or modal input.
 */
public interface InputContext {
    String name();
    ActionMap actionMap();
    int priority();
}
