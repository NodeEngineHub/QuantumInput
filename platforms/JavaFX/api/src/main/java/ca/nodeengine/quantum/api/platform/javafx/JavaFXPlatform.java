package ca.nodeengine.quantum.api.platform.javafx;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.platform.QuantumPlatform;

/**
 * Adds support for JavaFX
 *
 * @author FX
 */
public interface JavaFXPlatform extends QuantumPlatform {

    /**
     * The global JavaFX input device.
     */
    InputDevice JAVAFX_DEVICE = new InputDevice() {
        @Override
        public String name() {
            return "JavaFX";
        }

        @Override
        public boolean isGlobal() {
            return true;
        }
    };

    /**
     * Registers a JavaFX Scene with this platform.
     *
     * @param scene the JavaFX Scene to register
     */
    void registerScene(Object scene);

    /**
     * Unregisters a JavaFX Scene from this platform.
     *
     * @param scene the JavaFX Scene to unregister
     */
    void unregisterScene(Object scene);

    @Override
    default Class<?> getApiClass() {
        return JavaFXPlatform.class;
    }

    @Override
    default boolean usesGlobalDevice() {
        return true;
    }
}
