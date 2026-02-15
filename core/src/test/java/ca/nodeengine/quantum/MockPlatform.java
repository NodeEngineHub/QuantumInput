package ca.nodeengine.quantum;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.event.InputListener;
import ca.nodeengine.quantum.api.platform.QuantumPlatform;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;

/**
 * An abstract mock platform.
 *
 * @author FX
 */
@RequiredArgsConstructor
public abstract class MockPlatform implements QuantumPlatform {

    public static boolean USE_PER_DEVICE_PLATFORM = true;

    public static boolean ANY_INITIALIZED = false;
    public static boolean ANY_UPDATED = false;
    public static boolean ANY_TERMINATED = false;

    private final boolean global;
    private final Class<?> apiClass;

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
        return global || USE_PER_DEVICE_PLATFORM;
    }

    @Override
    public boolean usesGlobalDevice() {
        return global;
    }

    @Override
    public Class<?> getApiClass() {
        return apiClass;
    }

    @Override
    public Collection<InputDevice> getInputDevices() {
        return Collections.emptyList();
    }
}
