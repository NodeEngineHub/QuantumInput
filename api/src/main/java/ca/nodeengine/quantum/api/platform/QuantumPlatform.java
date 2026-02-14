package ca.nodeengine.quantum.api.platform;

import ca.nodeengine.quantum.api.InputEventListener;

import java.util.ServiceLoader;

/**
 * Represents an input platform (e.g., GLFW, JInput, etc.).
 * Platforms are responsible for generating {@link ca.nodeengine.quantum.api.InputEvent}s
 * and passing them to an {@link InputEventListener}.
 */
public interface QuantumPlatform {

    /**
     * Initializes the platform and sets the listener for input events.
     *
     * @param listener the listener to receive events
     */
    void initialize(InputEventListener listener);

    /**
     * Polls the platform for new input events.
     */
    void poll();

    /**
     * Terminates the platform and releases any resources.
     */
    void terminate();

    /**
     * Checks if the platform is supported on the current system.
     *
     * @return {@code true} if supported, otherwise {@code false}
     */
    boolean isSupported();

    /**
     * Creates a {@link ServiceLoader} for {@link QuantumPlatform}.
     *
     * @return a service loader for platforms
     */
    static ServiceLoader<QuantumPlatform> createServiceLoader() {
        return ServiceLoader.load(QuantumPlatform.class);
    }
}
