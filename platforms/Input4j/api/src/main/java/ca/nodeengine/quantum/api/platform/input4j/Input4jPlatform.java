package ca.nodeengine.quantum.api.platform.input4j;

import ca.nodeengine.quantum.api.platform.QuantumPlatform;

/**
 * Adds support for Input4j
 *
 * @author FX
 */
public interface Input4jPlatform extends QuantumPlatform {

    @Override
    default Class<?> getApiClass() {
        return Input4jPlatform.class;
    }

    @Override
    default boolean usesGlobalDevice() {
        return false;
    }
}
