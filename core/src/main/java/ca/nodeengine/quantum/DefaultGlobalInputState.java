package ca.nodeengine.quantum;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.state.GlobalInputState;
import ca.nodeengine.quantum.state.MutableInputState;
import org.jspecify.annotations.Nullable;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * The default implementation of {@link GlobalInputState} using a {@link BitSet} to contain all the codes.
 *
 * @author FX
 */
public class DefaultGlobalInputState implements GlobalInputState, MutableInputState {

    protected final BitSet currentlyDownKey = new BitSet(256); // Jumps to 512 if needed
    protected final BitSet previouslyDownKey = new BitSet(256);
    protected final BitSet currentlyDownButton = new BitSet(8);
    protected final BitSet previouslyDownButton = new BitSet(8);
    protected final @Nullable Map<Integer, Float> axisValues = createAxisMap();
    protected float[] mousePos = new float[2];
    protected float[] scroll = new float[2];

    /**
     * Allows you to disable axis states, or use a faster map implementation
     */
    protected @Nullable Map<Integer, Float> createAxisMap() {
        return new HashMap<>();
    }

    @Override
    public void update() {
        previouslyDownKey.clear();
        previouslyDownButton.clear();
        setScroll(null, 0, 0); // Reset scroll change
    }

    @Override
    public void reset() {
        currentlyDownKey.clear();
        previouslyDownKey.clear();
        currentlyDownButton.clear();
        previouslyDownButton.clear();
        if (axisValues != null) {
            axisValues.clear();
        }
        setMousePos(null, 0, 0);
        setScroll(null, 0, 0); // Reset scroll change
    }

    //region State checks

    @Override
    public boolean isKeyPressed(int code) {
        return currentlyDownKey.get(code) && !previouslyDownKey.get(code);
    }

    @Override
    public boolean isKeyHeld(int code) {
        return currentlyDownKey.get(code);
    }

    @Override
    public boolean isKeyReleased(int code) {
        return !currentlyDownKey.get(code) && previouslyDownKey.get(code);
    }

    @Override
    public boolean isButtonPressed(int code) {
        return currentlyDownButton.get(code) && !previouslyDownButton.get(code);
    }

    @Override
    public boolean isButtonHeld(int code) {
        return currentlyDownButton.get(code);
    }

    @Override
    public boolean isButtonReleased(int code) {
        return !currentlyDownButton.get(code) && previouslyDownButton.get(code);
    }

    @Override
    public float getAxis(int axisCode) {
        return axisValues == null ? 0F : axisValues.getOrDefault(axisCode, 0F);
    }

    @Override
    public float[] getMouse() {
        return this.mousePos;
    }

    @Override
    public float[] getScroll() {
        return this.scroll;
    }
    //endregion

    //region Mutation (For platform implementation)

    @Override
    public void setMousePos(@Nullable InputDevice device, float x, float y) {
        mousePos[0] = x;
        mousePos[1] = y;
    }

    @Override
    public void setScroll(@Nullable InputDevice device, float xOffset, float yOffset) {
        scroll[0] = xOffset;
        scroll[1] = yOffset;
    }

    @Override
    public void setKey(@Nullable InputDevice device, int code, boolean press) {
        if (press) {
            currentlyDownKey.set(code);
        } else {
            currentlyDownKey.clear(code);
            previouslyDownKey.set(code);
        }
    }

    @Override
    public void setButton(@Nullable InputDevice device, int code, boolean press) {
        if (press) {
            currentlyDownButton.set(code);
        } else {
            currentlyDownButton.clear(code);
            previouslyDownButton.set(code);
        }
    }

    @Override
    public void setAxis(@Nullable InputDevice device, int axis, float value) {
        if (axisValues != null) {
            axisValues.put(axis, value);
        }
    }
    //endregion
}
