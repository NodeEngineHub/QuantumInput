package ca.nodeengine.quantum.api.action;

import java.util.List;

/**
 * A collection of {@link ActionBinding}s.
 * <p>
 * An ActionMap defines how multiple actions are mapped to hardware inputs.
 * </p>
 *
 * @author FX
 */
public interface ActionMap {

    /**
     * Gets all bindings associated with a specific action name.
     *
     * @param action The name of the action.
     * @return A list of bindings for the specified action.
     */
    List<ActionBinding> getBindings(String action);
}
