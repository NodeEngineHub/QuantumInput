package ca.nodeengine.quantum.platform.jogl;

import ca.nodeengine.quantum.InputDevice;
import ca.nodeengine.quantum.platform.QuantumPlatform;

/**
 * Adds support for JOGL
 *
 * @author FX
 */
public interface JOGLPlatform extends QuantumPlatform {

    /**
     * The global JOGL input device.
     */
    InputDevice JOGL_DEVICE = new InputDevice() {
        @Override
        public String name() {
            return "JOGL";
        }

        @Override
        public boolean isGlobal() {
            return true;
        }
    };

    void registerWindow(Object window);

    /**
     * Unregisters a JOGL window from this platform.
     *
     * @param window the JOGL window to unregister
     */
    void unregisterWindow(Object window);

    @Override
    default Class<?> getApiClass() {
        return JOGLPlatform.class;
    }

    @Override
    default boolean usesGlobalDevice() {
        return true;
    }
}
