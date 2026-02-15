package ca.nodeengine.quantum.api;

import ca.nodeengine.quantum.api.event.InputListener;
import ca.nodeengine.quantum.api.exception.QuantumInputException;
import ca.nodeengine.quantum.api.platform.QuantumPlatform;
import ca.nodeengine.quantum.api.state.GlobalInputState;
import ca.nodeengine.quantum.api.state.InputState;
import ca.nodeengine.quantum.api.state.PerDeviceInputState;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;

/**
 * InputSystem is the main interface in QuantumInput.
 * <p>
 * It handles what is accessible to developers, allowing them to set up their environment and usage.
 * It manages platforms, input processors, and listeners.
 * </p>
 *
 * @param <IS> The type of the input state.
 * @author FX
 */
public interface InputSystem<IS extends InputState> extends AutoCloseable {

    //region Lifecycle

    /**
     * Polls devices and processes events.
     * <p>
     * This should be called periodically in the application's main loop.
     * </p>
     */
    void update();
    //endregion

    //region Getters

    /**
     * Gets the input state.
     * <p>
     * This is the internal state used to keep track of input states.
     * Use this if you want to use polling-based inputs.
     * </p>
     *
     * @return The input state.
     */
    IS state();

    /**
     * Gets a platform from its API class.
     *
     * @param apiClass The API class to get the platform of.
     * @param <P>      The platform API type.
     * @return The platform implementation, or {@code null} if not found.
     */
    <P extends QuantumPlatform> @Nullable P getPlatform(Class<P> apiClass);

    /**
     * Gets a collection of platforms within this input system.
     * <p>
     * This is accessible to help debug common issues.
     * </p>
     *
     * @return A collection of platforms.
     */
    Collection<QuantumPlatform> getPlatforms();
    //endregion

    //region Listeners

    /**
     * Adds an input listener to this input system.
     * <p>
     * Used for event-based input processing.
     * </p>
     *
     * @param listener The input listener to add.
     */
    void addListener(InputListener listener);
    //endregion

    /**
     * Creates an input system with a global input state.
     *
     * @return The global input system.
     * @throws QuantumInputException If any platform doesn't support global devices.
     */
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

    /**
     * Creates an input system with a per-device input state.
     *
     * @return The per-device input system.
     * @throws QuantumInputException If any platform doesn't support per-device access.
     */
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

    /**
     * Creates a new instance of an InputSystem.
     *
     * @return A new InputSystem instance.
     * @throws NoSuchElementException If no InputSystem implementation is found.
     */
    static InputSystem<?> createInputSystem() {
        return ServiceLoader.load(InputSystem.class).findFirst().orElseThrow();
    }
}
