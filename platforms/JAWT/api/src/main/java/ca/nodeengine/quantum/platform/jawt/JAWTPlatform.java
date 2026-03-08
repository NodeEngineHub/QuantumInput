package ca.nodeengine.quantum.platform.jawt;

import ca.nodeengine.quantum.InputDevice;
import ca.nodeengine.quantum.platform.QuantumPlatform;

import java.awt.Component;

/**
 * Adds support for JAWT (AWT/Swing)
 *
 * @author FX
 */
public interface JAWTPlatform extends QuantumPlatform {

    /**
     * The global JAWT input device.
     */
    InputDevice JAWT_DEVICE = new InputDevice() {
        @Override
        public String name() {
            return "JAWT";
        }

        @Override
        public boolean isGlobal() {
            return true;
        }
    };

    /**
     * Registers an AWT/Swing Component with this platform.
     *
     * @param component the Component to register
     */
    void registerComponent(Component component);

    /**
     * Unregisters an AWT/Swing Component from this platform.
     *
     * @param component the Component to unregister
     */
    void unregisterComponent(Component component);

    @Override
    default Class<?> getApiClass() {
        return JAWTPlatform.class;
    }

    @Override
    default boolean usesGlobalDevice() {
        return true;
    }
}
