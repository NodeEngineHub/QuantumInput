package ca.nodeengine.quantum;

import ca.nodeengine.quantum.platform.QuantumPlatform;
import ca.nodeengine.quantum.state.GlobalInputState;
import ca.nodeengine.quantum.state.MutableInputState;
import ca.nodeengine.quantum.state.PerDeviceInputState;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

/**
 * Adapter that makes a {@link PerDeviceInputState} compatible with {@link GlobalInputState}.<br>
 * This comes at a performance cost, as it requires iterating over all platforms and devices for every check.
 *
 * @author FX
 */
@RequiredArgsConstructor
public class PerDeviceToGlobalInputStateAdapter implements GlobalInputState, MutableInputState {

    private final PerDeviceInputState perDeviceState;
    private final Collection<QuantumPlatform> platforms;

    private MutableInputState mutableState() {
        if (perDeviceState instanceof MutableInputState mutable) {
            return mutable;
        }
        throw new IllegalStateException("The wrapped PerDeviceInputState must implement MutableInputState");
    }

    @Override
    public boolean isKeyPressed(int code) {
        for (QuantumPlatform platform : platforms) {
            for (InputDevice device : platform.getInputDevices()) {
                if (perDeviceState.isKeyPressed(device, code)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isKeyHeld(int code) {
        for (QuantumPlatform platform : platforms) {
            for (InputDevice device : platform.getInputDevices()) {
                if (perDeviceState.isKeyHeld(device, code)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isKeyReleased(int code) {
        for (QuantumPlatform platform : platforms) {
            for (InputDevice device : platform.getInputDevices()) {
                if (perDeviceState.isKeyReleased(device, code)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isButtonPressed(int code) {
        for (QuantumPlatform platform : platforms) {
            for (InputDevice device : platform.getInputDevices()) {
                if (perDeviceState.isButtonPressed(device, code)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isButtonHeld(int code) {
        for (QuantumPlatform platform : platforms) {
            for (InputDevice device : platform.getInputDevices()) {
                if (perDeviceState.isButtonHeld(device, code)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isButtonReleased(int code) {
        for (QuantumPlatform platform : platforms) {
            for (InputDevice device : platform.getInputDevices()) {
                if (perDeviceState.isButtonReleased(device, code)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public float getAxis(int axisCode) {
        for (QuantumPlatform platform : platforms) {
            for (InputDevice device : platform.getInputDevices()) {
                float axis = perDeviceState.getAxis(device, axisCode);
                if (axis != 0) {
                    return axis;
                }
            }
        }
        return 0;
    }

    @Override
    public float[] getMouse() {
        for (QuantumPlatform platform : platforms) {
            for (InputDevice device : platform.getInputDevices()) {
                float[] pos = perDeviceState.getMouse(device);
                if (pos[0] != 0 || pos[1] != 0) {
                    return pos;
                }
            }
        }
        return new float[2];
    }

    @Override
    public float[] getScroll() {
        for (QuantumPlatform platform : platforms) {
            for (InputDevice device : platform.getInputDevices()) {
                float[] amt = perDeviceState.getScroll(device);
                if (amt[0] != 0 || amt[1] != 0) {
                    return amt;
                }
            }
        }
        return new float[2];
    }

    @Override
    public void update() {
        perDeviceState.update();
    }

    @Override
    public void reset() {
        perDeviceState.reset();
    }

    @Override
    public void setMousePos(InputDevice device, float x, float y) {
        mutableState().setMousePos(device, x, y);
    }

    @Override
    public void setScroll(InputDevice device, float xOffset, float yOffset) {
        mutableState().setScroll(device, xOffset, yOffset);
    }

    @Override
    public void setKey(InputDevice device, int code, boolean press) {
        mutableState().setKey(device, code, press);
    }

    @Override
    public void setButton(InputDevice device, int code, boolean press) {
        mutableState().setButton(device, code, press);
    }

    @Override
    public void setAxis(InputDevice device, int axis, float value) {
        mutableState().setAxis(device, axis, value);
    }
}
