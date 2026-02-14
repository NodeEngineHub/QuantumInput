package ca.nodeengine.quantum.api.platform;

import ca.nodeengine.quantum.api.event.InputListener;
import ca.nodeengine.quantum.api.event.InputEvent;

/**
 * Represents an input platform (e.g., GLFW, JInput, etc.).
 * Platforms are responsible for generating {@link InputEvent}s
 * and passing them to an {@link InputListener}.
 */
public interface QuantumPlatform extends AutoCloseable {

    //region Lifecycle

    /**
     * Initializes the platform and sets the listener for input events.
     *
     * @param listener the listener to receive events
     */
    void initialize(InputListener listener);

    /**
     * Poll devices and process events
     */
    void update();
    //endregion

    /**
     * Checks if the platform is supported on the current system.
     *
     * @return {@code true} if supported, otherwise {@code false}
     */
    boolean isSupported();

    /**
     * Determines if this platform uses a global input device or if it supports per-device inputs.
     *
     * @return {@code true} if it uses a global device, otherwise {@code false}
     */
    boolean usesGlobalDevice();

    /**
     * The API class.<br>
     * This is the class which is accessible to people using the platform api.<br>
     * It's the class type used to get the platform.<br>
     * This class must extend the API class.
     *
     * @return The API class.
     */
    Class<?> getApiClass();
}
