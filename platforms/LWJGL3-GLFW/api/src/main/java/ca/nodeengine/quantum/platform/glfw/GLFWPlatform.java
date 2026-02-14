package ca.nodeengine.quantum.platform.glfw;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.platform.QuantumPlatform;

/**
 * Adds support for GLFW through LWJGL3
 *
 * @author FX
 */
public interface GLFWPlatform extends QuantumPlatform {

    InputDevice GLFW_DEVICE = new InputDevice() {
        @Override
        public String name() {
            return "GLFW";
        }

        @Override
        public boolean isGlobal() {
            return true;
        }
    };

    /**
     * Registers a window with any platform that supports it.
     *
     * @param windowHandle the handle of the window to register
     */
    void registerWindow(long windowHandle);

    /**
     * Unregisters a window from any platform that supports it.
     *
     * @param windowHandle the handle of the window to unregister
     */
    void unregisterWindow(long windowHandle);

    @Override
    default Class<?> getApiClass() {
        return GLFWPlatform.class;
    }

    @Override
    default boolean usesGlobalDevice() {
        return true;
    }
}
