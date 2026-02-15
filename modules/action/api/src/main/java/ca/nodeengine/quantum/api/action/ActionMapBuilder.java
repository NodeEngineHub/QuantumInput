package ca.nodeengine.quantum.api.action;

import ca.nodeengine.quantum.api.InputDevice;
import org.jspecify.annotations.Nullable;
import java.util.ServiceLoader;

/**
 * A builder for creating {@link ActionMap} instances.
 */
public interface ActionMapBuilder {

    /**
     * Creates a new instance of an ActionMapBuilder.
     *
     * @return A new ActionMapBuilder instance.
     */
    static ActionMapBuilder create() {
        return ServiceLoader.load(ActionMapBuilder.class)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No ActionMapBuilder implementation found"));
    }

    /**
     * Adds a digital binding to the action map.
     *
     * @param actionName The name of the action.
     * @param code The input code (key or button).
     * @return This builder.
     */
    ActionMapBuilder add(String actionName, int code);

    /**
     * Adds a digital binding for a specific device to the action map.
     *
     * @param actionName The name of the action.
     * @param device The input device, or {@code null} for global.
     * @param code The input code (key or button).
     * @return This builder.
     */
    ActionMapBuilder add(String actionName, @Nullable InputDevice device, int code);

    /**
     * Builds the action map.
     *
     * @return The created action map.
     */
    ActionMap build();
}
