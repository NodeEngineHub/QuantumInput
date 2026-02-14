package ca.nodeengine.quantum.platform.jinput;

import ca.nodeengine.quantum.api.InputEventListener;
import ca.nodeengine.quantum.api.platform.QuantumPlatform;

public class JInputPlatform implements QuantumPlatform {
    @Override
    public void initialize(InputEventListener listener) {
        // Implementation for JInput
    }

    @Override
    public void poll() {
        // Implementation for JInput
    }

    @Override
    public void terminate() {
        // Implementation for JInput
    }

    @Override
    public boolean isSupported() {
        return true;
    }
}
