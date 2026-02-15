package ca.nodeengine.quantum.api;

/**
 * Represents an input device.
 * <p>
 * Some platforms may allow you to get the different input devices, while others use a global input device.
 * </p>
 *
 * @author FX
 */
public interface InputDevice {

    /**
     * Get the input device's name.
     * <p>
     * This doesn't need to be unique, it's to display to the user.
     * </p>
     *
     * @return The input device's display name.
     */
    String name();

    /**
     * Check if this input device is a global device.
     * <p>
     * Global devices are virtual devices for platforms which don't provide device specific access.
     * They represent all devices as one.
     * </p>
     *
     * @return {@code true} if this is a global input device, otherwise {@code false}
     */
    boolean isGlobal();
}
