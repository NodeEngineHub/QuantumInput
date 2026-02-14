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
    void popContext(String name);

    List<InputContext> getActiveContexts();
}
