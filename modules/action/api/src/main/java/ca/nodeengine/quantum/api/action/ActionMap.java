package ca.nodeengine.quantum.api.action;

import java.util.List;

/**
 * Maps actions to bindings.
 */
public interface ActionMap {
    List<ActionBinding> getBindings(String action);
}
