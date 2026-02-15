package ca.nodeengine.quantum.api.context;

import ca.nodeengine.quantum.api.action.ActionMap;

import java.util.ServiceLoader;

/**
 * A builder for creating {@link InputContextManager} instances.
 *
 * @author FX
 */
public interface InputContextManagerBuilder {

    /**
     * Creates a new instance of an InputContextManagerBuilder.
     *
     * @return A new InputContextManagerBuilder instance.
     */
    static InputContextManagerBuilder create() {
        return ServiceLoader.load(InputContextManagerBuilder.class)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No InputContextManagerBuilder implementation found"));
    }

    /**
     * Adds an initial context to the manager.
     *
     * @param context The context to add.
     * @return This builder.
     */
    InputContextManagerBuilder addContext(InputContext context);

    /**
     * Adds an initial context to the manager.
     *
     * @param name The name of the context.
     * @param actionMap The action map for the context.
     * @param priority The priority of the context.
     * @return This builder.
     */
    InputContextManagerBuilder addContext(String name, ActionMap actionMap, int priority);

    /**
     * Builds the input context manager.
     *
     * @return The created input context manager.
     */
    InputContextManager build();
}
