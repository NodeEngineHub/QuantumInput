package ca.nodeengine.quantum.platform.jinput;

import ca.nodeengine.quantum.platform.QuantumPlatform;

/**
 * Adds support for JInput
 *
 * @author FX
 */
public interface JInputPlatform extends QuantumPlatform {

    @Override
    default Class<?> getApiClass() {
        return JInputPlatform.class;
    }

    @Override
    default boolean usesGlobalDevice() {
        return false;
    }
}
