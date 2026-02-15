package ca.nodeengine.quantum.api.platform;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.InputSystem;
import ca.nodeengine.quantum.api.event.InputListener;
import ca.nodeengine.quantum.api.event.InputEvent;

import java.util.Collection;

/**
 * Represents an input platform.
 * <p>
 * Platforms are responsible for polling hardware, generating {@link InputEvent}s,
 * and passing them to an {@link InputListener}.
 * </p>
 *
 * @author FX
 */
public interface QuantumPlatform extends AutoCloseable {

    //region Lifecycle

    /**
     * Initializes the platform and sets the listener for input events.
     *
     * @param listener The listener to receive events.
     */
    void initialize(InputListener listener);

    /**
     * Polls devices and processes events.
     * <p>
     * This is called by the {@link InputSystem}.
     * </p>
     */
    void update();
    //endregion

    /**
     * Checks if the platform is supported on the current system.
     *
     * @return {@code true} if supported, otherwise {@code false}.
     */
    boolean isSupported();

    /**
     * Determines if this platform uses a global input device or if it supports per-device inputs.
     *
     * @return {@code true} if it uses a global device, otherwise {@code false}.
     */
    boolean usesGlobalDevice();

    /**
     * Gets the input devices associated with this platform.
     *
     * @return A collection of input devices.
     */
    Collection<InputDevice> getInputDevices();

    /**
     * Gets the API class of this platform.
     * <p>
     * This is the class which is accessible to people using the platform API.
     * It's the class type used to retrieve the platform instance from the system.
     * This class must extend the API class.
     * </p>
     *
     * @return The API class.
     */
    Class<?> getApiClass();
}
