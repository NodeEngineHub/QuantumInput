package ca.nodeengine.quantum;

import ca.nodeengine.quantum.api.InputEventListener;
import ca.nodeengine.quantum.api.platform.WindowPlatform;

public class MockPlatform implements WindowPlatform {
    public static boolean ANY_INITIALIZED = false;
    public static boolean ANY_POLLED = false;
    public static boolean ANY_TERMINATED = false;
    public static long LAST_REGISTERED_WINDOW = -1;
    public static long LAST_UNREGISTERED_WINDOW = -1;

    public boolean initialized = false;
    public boolean polled = false;
    public boolean terminated = false;

    @Override
    public void registerWindow(long windowHandle) {
        LAST_REGISTERED_WINDOW = windowHandle;
    }

    @Override
    public void unregisterWindow(long windowHandle) {
        LAST_UNREGISTERED_WINDOW = windowHandle;
    }

    @Override
    public void initialize(InputEventListener listener) {
        initialized = true;
        ANY_INITIALIZED = true;
    }

    @Override
    public void poll() {
        polled = true;
        ANY_POLLED = true;
    }

    @Override
    public void terminate() {
        terminated = true;
        ANY_TERMINATED = true;
    }

    @Override
    public boolean isSupported() {
        return true;
    }
}
