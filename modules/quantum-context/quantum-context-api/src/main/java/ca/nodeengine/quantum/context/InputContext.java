package ca.nodeengine.quantum.context;

import ca.nodeengine.quantum.action.ActionMap;

/**
 * Contexts allow layered or modal input.
 *
 * @author FX
 */
public interface InputContext {

    /**
     * The name of this input context
     *
     * @return The name
     */
    String name();

    /**
     * This input context's action map.
     *
     * @return The action map
     */
    ActionMap actionMap();

    /**
     * The context's priority.<br>
     * Lower priority will run first.
     *
     * @return The priority
     */
    int priority();
}
