package ca.nodeengine.quantum.api.action;

import java.util.List;

/**
 * Maps actions to bindings.
 */
public interface ActionMap {
    InputAction getAction(String name);
    List<ActionBinding> getBindings(InputAction action);
}
