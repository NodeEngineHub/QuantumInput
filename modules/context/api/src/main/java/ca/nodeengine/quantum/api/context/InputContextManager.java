package ca.nodeengine.quantum.api.context;

import java.util.List;

/**
 * Context Stack / Resolver
 * Determines which context “wins”.
 *
 * @see ContextRules
 */
public interface InputContextManager {
    void pushContext(InputContext context);

    /**
     * Activates a registered context by name.
     *
     * @param name The name of the context to activate.
     */
    void pushContext(String name);

    /**
     * Deactivates an active context by name.
     *
     * @param name The name of the context to deactivate.
     */
    void popContext(String name);

    List<InputContext> getActiveContexts();
}
