package ca.nodeengine.quantum;

import ca.nodeengine.quantum.api.InputCode;
import ca.nodeengine.quantum.api.InputState;
import ca.nodeengine.quantum.api.MutableInputState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Default {@link InputState}
 *
 * @author FX
 */
public class DefaultInputState implements MutableInputState {

    private final Set<InputCode> currentlyDown = new HashSet<>();
    private final Map<InputCode, Float> axisValues = new HashMap<>();

    @Override
    public boolean isDown(InputCode code) {
        return currentlyDown.contains(code);
    }

    @Override
    public float getAxis(InputCode axis) {
        return axisValues.getOrDefault(axis, 0.0f);
    }

    @Override
    public void poll() {}

    @Override
    public void setDown(InputCode code, boolean down) {
        if (down) {
            currentlyDown.add(code);
        } else {
            currentlyDown.remove(code);
        }
    }

    @Override
    public void setAxis(InputCode axis, float value) {
        axisValues.put(axis, value);
    }
}
