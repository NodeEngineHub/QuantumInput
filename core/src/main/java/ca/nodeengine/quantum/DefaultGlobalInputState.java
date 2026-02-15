package ca.nodeengine.quantum;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.InputProcessor;
import ca.nodeengine.quantum.api.state.GlobalInputState;
import ca.nodeengine.quantum.state.MutableInputState;
import org.jspecify.annotations.Nullable;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * The default implementation of {@link GlobalInputState} using a {@link BitSet} to track key and button states.
 * <p>
 * This class also implements {@link MutableInputState} to allow updates from {@link InputProcessor}.
 * </p>
 *
 * @author FX
 */
public class DefaultGlobalInputState implements GlobalInputState, MutableInputState {

    /** BitSet containing keys currently pressed. */
    protected final BitSet currentlyDownKey = new BitSet(256);
    /** BitSet containing keys down in the previous frame. */
    protected final BitSet wasDownKey = new BitSet(256);
    /** BitSet containing keys released during the last update. */
    protected final BitSet justReleasedKey = new BitSet(256);
    /** BitSet containing mouse buttons currently pressed. */
    protected final BitSet currentlyDownButton = new BitSet(8);
    /** BitSet containing buttons down in the previous frame. */
    protected final BitSet wasDownButton = new BitSet(8);
    /** BitSet containing mouse buttons released during the last update. */
    protected final BitSet justReleasedButton = new BitSet(8);
    /** Map containing analog axis values. */
    protected final @Nullable Map<Integer, Float> axisValues = createAxisMap();
    /** Array containing the mouse [x, y] position. */
    protected float[] mousePos = new float[2];
    /** Array containing the mouse [x, y] scroll velocity. */
    protected float[] scroll = new float[2];

    /**
     * Creates the map used to store axis values.
     * <p>
     * Override this to provide a faster map implementation or to return {@code null} if axes are not needed.
     * </p>
     *
     * @return A map for axis values.
     */
    protected @Nullable Map<Integer, Float> createAxisMap() {
        return new HashMap<>();
    }

    @Override
    public void update() {
        wasDownKey.clear();
        wasDownKey.or(currentlyDownKey);
        wasDownButton.clear();
        wasDownButton.or(currentlyDownButton);

        justReleasedKey.clear();
        justReleasedButton.clear();
        setScroll(null, 0, 0); // Reset scroll change
    }

    @Override
    public void reset() {
        currentlyDownKey.clear();
        wasDownKey.clear();
        justReleasedKey.clear();
        currentlyDownButton.clear();
        wasDownButton.clear();
        justReleasedButton.clear();
        if (axisValues != null) {
            axisValues.clear();
        }
        setMousePos(null, 0, 0);
        setScroll(null, 0, 0); // Reset scroll change
    }

    //region State checks

    @Override
    public boolean isKeyPressed(int code) {
        return currentlyDownKey.get(code) && !wasDownKey.get(code);
    }

    @Override
    public boolean isKeyHeld(int code) {
        return currentlyDownKey.get(code);
    }

    @Override
    public boolean isKeyReleased(int code) {
        return justReleasedKey.get(code);
    }

    @Override
    public boolean isButtonPressed(int code) {
        return currentlyDownButton.get(code) && !wasDownButton.get(code);
    }

    @Override
    public boolean isButtonHeld(int code) {
        return currentlyDownButton.get(code);
    }

    @Override
    public boolean isButtonReleased(int code) {
        return justReleasedButton.get(code);
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
            justReleasedKey.set(code);
        }
    }

    @Override
    public void setButton(@Nullable InputDevice device, int code, boolean press) {
        if (press) {
            currentlyDownButton.set(code);
        } else {
            currentlyDownButton.clear(code);
            justReleasedButton.set(code);
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
