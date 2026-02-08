package ca.nodeengine.quantum;

import ca.nodeengine.quantum.api.InputCode;
import ca.nodeengine.quantum.api.InputState;
import ca.nodeengine.quantum.api.MutableInputState;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link InputState} using a {@link BitSet} to contain all the codes.<br>
 * This isn't the default as I'm unsure exactly how many codes are possible,
 * mostly since the backend implementation has no limits on codes.
 *
 * @author FX
 */
public class BitSetInputState implements MutableInputState {

    protected final BitSet currentlyDown = new BitSet(512); // 512 scancodes
    private final Map<InputCode, Float> axisValues = new HashMap<>();

    @Override
    public boolean isDown(InputCode code) {
        return currentlyDown.get(code.code());
    }

    @Override
    public float getAxis(InputCode axis) {
        return axisValues.getOrDefault(axis, 0.0f);
    }

    @Override
    public void poll() {}

    @Override
    public void setDown(InputCode code, boolean down) {
        currentlyDown.set(code.code(), down);
    }

    @Override
    public void setAxis(InputCode axis, float value) {
        axisValues.put(axis, value);
    }
}
