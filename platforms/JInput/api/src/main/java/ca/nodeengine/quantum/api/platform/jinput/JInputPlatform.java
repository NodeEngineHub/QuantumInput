package ca.nodeengine.quantum.api.platform.jinput;

import ca.nodeengine.quantum.api.platform.QuantumPlatform;

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
