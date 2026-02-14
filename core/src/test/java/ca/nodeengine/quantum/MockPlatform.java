package ca.nodeengine.quantum;

import ca.nodeengine.quantum.api.event.InputListener;
import ca.nodeengine.quantum.api.platform.QuantumPlatform;

public class MockPlatform implements QuantumPlatform {
    public static boolean ANY_INITIALIZED = false;
    public static boolean ANY_UPDATED = false;
    public static boolean ANY_TERMINATED = false;

    public boolean initialized = false;
    public boolean updated = false;
    public boolean terminated = false;

    @Override
    public void initialize(InputListener listener) {
        initialized = true;
        ANY_INITIALIZED = true;
    }

    @Override
    public void update() {
        updated = true;
        ANY_UPDATED = true;
    }

    @Override
    public void close() {
        terminated = true;
        ANY_TERMINATED = true;
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public boolean usesGlobalDevice() {
        return true;
    }

    @Override
    public Class<?> getApiClass() {
        return MockPlatform.class;
    }
}
