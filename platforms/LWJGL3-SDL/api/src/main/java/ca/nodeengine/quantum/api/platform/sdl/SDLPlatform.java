package ca.nodeengine.quantum.api.platform.sdl;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.platform.QuantumPlatform;

/**
 * Adds support for SDL through LWJGL3
 *
 * @author FX
 */
public interface SDLPlatform extends QuantumPlatform {

    InputDevice SDL_DEVICE = new InputDevice() {
        @Override
        public String name() {
            return "SDL";
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
        return SDLPlatform.class;
    }

    @Override
    default boolean usesGlobalDevice() {
        return true;
    }
}
