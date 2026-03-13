package ca.nodeengine.quantum;

import ca.nodeengine.quantum.state.GlobalInputState;
import ca.nodeengine.quantum.state.MutableInputState;
import ca.nodeengine.quantum.state.PerDeviceInputState;
import lombok.RequiredArgsConstructor;

/**
 * Adapter that makes a {@link GlobalInputState} compatible with {@link PerDeviceInputState}.
 *
 * @author FX
 */
@RequiredArgsConstructor
public class GlobalToPerDeviceInputStateAdapter implements PerDeviceInputState, MutableInputState {

    private final GlobalInputState globalState;

    @Override
    public boolean isKeyPressed(InputDevice device, int code) {
        return globalState.isKeyPressed(code);
    }

    @Override
    public boolean isKeyHeld(InputDevice device, int code) {
        return globalState.isKeyHeld(code);
    }

    @Override
    public boolean isKeyReleased(InputDevice device, int code) {
        return globalState.isKeyReleased(code);
    }

    @Override
    public boolean isButtonPressed(InputDevice device, int code) {
        return globalState.isButtonPressed(code);
    }

    @Override
    public boolean isButtonHeld(InputDevice device, int code) {
        return globalState.isButtonHeld(code);
    }

    @Override
    public boolean isButtonReleased(InputDevice device, int code) {
        return globalState.isButtonReleased(code);
    }

    @Override
    public float getAxis(InputDevice device, int axisCode) {
        return globalState.getAxis(axisCode);
    }

    @Override
    public float[] getMouse(InputDevice device) {
        return globalState.getMouse();
    }

    @Override
    public float[] getScroll(InputDevice device) {
        return globalState.getScroll();
    }

    @Override
    public void update() {
        globalState.update();
    }

    @Override
    public void reset() {
        globalState.reset();
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

    private MutableInputState mutableState() {
        if (globalState instanceof MutableInputState mutable) {
            return mutable;
        }
        throw new IllegalStateException("The wrapped GlobalInputState must implement MutableInputState");
    }
}
