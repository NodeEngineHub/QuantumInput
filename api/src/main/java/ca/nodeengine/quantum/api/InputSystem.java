package ca.nodeengine.quantum.api;

import ca.nodeengine.quantum.api.event.InputListener;
import ca.nodeengine.quantum.api.exception.QuantumInputException;
import ca.nodeengine.quantum.api.platform.QuantumPlatform;
import ca.nodeengine.quantum.api.state.GlobalInputState;
import ca.nodeengine.quantum.api.state.InputState;
import ca.nodeengine.quantum.api.state.PerDeviceInputState;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.ServiceLoader;

/**
 * InputSystem is the main class in Quantum.<br>
 * It handles what is accessible to developers, allowing them to setup their environment and usages.
 *
 * @author FX
 */
public interface InputSystem<IS extends InputState> extends AutoCloseable {

    //region Lifecycle

    /**
     * Poll devices and process events
     */
    void update();
    //endregion

    //region Getters

    /**
     * The input state.<br>
     * This is the internal state used to keep track of input states.<br>
     * Use this if you want to use polling based inputs.
     *
     * @return The input state.
     */
    IS state();

    /**
     * Get a platform from its API class.
     *
     * @param apiClass The API class to get the platform of.
     * @return The Platform implementation.
     * @param <P> The platform API Type
     */
    <P extends QuantumPlatform> @Nullable P getPlatform(Class<P> apiClass);

    /**
     * Get a collection of platforms within this input system.<br>
     * This is accessible to help debug common issues.
     *
     * @return A collection of platforms.
     */
    Collection<QuantumPlatform> getPlatforms();
    //endregion

    //region Listeners

    /**
     * Add an input listener to this input system.<br>
     * Used for event-based input processing
     *
     * @param listener The input listener
     */
    void addListener(InputListener listener);
    //endregion


    static InputSystem<GlobalInputState> createGlobalInputSystem() {
        InputSystem<?> inputSystem = createInputSystem();
        try {
            //noinspection unchecked
            return (InputSystem<GlobalInputState>) inputSystem;
        } catch (ClassCastException e) {
            StringBuilder builder = new StringBuilder("Unable to use InputSystem with global input state, " +
                    "the following platforms don't use global devices: [");
            boolean addComma = false;
            for (QuantumPlatform platform : inputSystem.getPlatforms()) {
                if (!platform.usesGlobalDevice()) {
                    if (addComma) {
                        builder.append(", ");
                    }
                    builder.append(platform.getClass().getSimpleName());
                    addComma = true;
                }
            }
            builder.append("]");
            throw new QuantumInputException(builder.toString());
        }
    }

    static InputSystem<PerDeviceInputState> createPerDeviceInputSystem() {
        InputSystem<?> inputSystem = createInputSystem();
        try {
            //noinspection unchecked
            return (InputSystem<PerDeviceInputState>) inputSystem;
        } catch (ClassCastException e) {
            StringBuilder builder = new StringBuilder("Unable to use InputSystem with per-device input state, " +
                    "all the devices are global: [");
            boolean addComma = false;
            for (QuantumPlatform platform : inputSystem.getPlatforms()) {
                if (!platform.usesGlobalDevice()) {
                    throw new QuantumInputException("Failed to cast to PerDeviceInputState!");
                }
                if (addComma) {
                    builder.append(", ");
                }
                builder.append(platform.getClass().getSimpleName());
                addComma = true;
            }
            builder.append("]");
            throw new QuantumInputException(builder.toString());
        }
    }

    static InputSystem<?> createInputSystem() {
        return ServiceLoader.load(InputSystem.class).findFirst().orElseThrow();
    }
}
