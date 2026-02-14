package ca.nodeengine.quantum.api.platform;

/**
 * Represents a platform that requires window registration for input capturing.
 */
public interface WindowPlatform extends QuantumPlatform {

    /**
     * Registers a window with the platform.
     *
     * @param windowHandle the handle of the window to register
     */
    void registerWindow(long windowHandle);

    /**
     * Unregisters a window from the platform.
     *
     * @param windowHandle the handle of the window to unregister
     */
    void unregisterWindow(long windowHandle);
}
