package ca.nodeengine.quantum;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.state.PerDeviceInputState;
import ca.nodeengine.quantum.state.MutableInputState;
import org.jspecify.annotations.Nullable;

import java.util.*;

/**
 * The default implementation of {@link PerDeviceInputState}.
 *
 * @author FX
 */
public class DefaultPerDeviceInputState implements PerDeviceInputState, MutableInputState {

    protected static final int PREVIOUS = 0;
    protected static final int CURRENT = 1;

    protected static final float[] EMPTY = new float[] {0, 0};

    protected final Map<InputDevice, Set<Integer>[]> deviceKeyMap = createDeviceKeyMap();
    protected final Map<InputDevice, Set<Integer>[]> deviceButtonMap = createDeviceButtonMap();
    protected final @Nullable Map<InputDevice, Map<Integer, Float>> deviceAxisMap = createDeviceAxisMap();
    protected final Map<InputDevice, float[]> deviceMouseMap = createMouseAndScrollMap();
    protected final Map<InputDevice, float[]> deviceScrollMap = createMouseAndScrollMap();

    //region Default creators

    /**
     * Allows you to use a faster map implementation by overriding this method.
     */
    protected Map<InputDevice, Set<Integer>[]> createDeviceKeyMap() {
        return new HashMap<>();
    }

    /**
     * Allows you to use a faster map implementation by overriding this method.
     */
    protected Map<InputDevice, Set<Integer>[]> createDeviceButtonMap() {
        return new HashMap<>();
    }

    /**
     * Allows you to use a faster set implementation by overriding this method.
     */
    protected Set<Integer> createKeyIntSet() {
        return new HashSet<>();
    }

    /**
     * Allows you to use a faster set implementation by overriding this method.
     */
    protected Set<Integer> createButtonIntSet() {
        return new HashSet<>();
    }

    /**
     * Allows you to disable axis states, or use a faster map implementation
     */
    protected @Nullable Map<InputDevice, Map<Integer, Float>> createDeviceAxisMap() {
        return new HashMap<>();
    }

    /**
     * Allows you to use a faster map implementation by overriding this method.
     */
    protected Map<Integer, Float> createAxisMap() {
        return new HashMap<>();
    }

    /**
     * Allows you to use a faster map implementation by overriding this method.<br>
     * This is called twice, once for the mouse map and once for the scroll map.
     */
    protected Map<InputDevice, float[]> createMouseAndScrollMap() {
        return new HashMap<>();
    }
    //endregion

    @Override
    public void update() {
        for (Map.Entry<InputDevice, Set<Integer>[]> entry : deviceKeyMap.entrySet()) {
            entry.getValue()[PREVIOUS].clear();
        }
        for (Map.Entry<InputDevice, Set<Integer>[]> entry : deviceButtonMap.entrySet()) {
            entry.getValue()[PREVIOUS].clear();
        }
    }

    @Override
    public void reset() {
        deviceKeyMap.clear();
        deviceButtonMap.clear();
        if (deviceAxisMap != null) {
            deviceAxisMap.clear();
        }
        deviceMouseMap.clear();
        deviceScrollMap.clear();
    }

    //region State checks

    @Override
    public boolean isKeyPressed(InputDevice device, int code) {
        Set<Integer>[] states = deviceKeyMap.get(device);
        if (states == null) {
            return false;
        }
        return states[CURRENT].contains(code) && !states[PREVIOUS].contains(code);
    }

    @Override
    public boolean isKeyHeld(InputDevice device, int code) {
        Set<Integer>[] states = deviceKeyMap.get(device);
        if (states == null) {
            return false;
        }
        return states[CURRENT].contains(code);
    }

    @Override
    public boolean isKeyReleased(InputDevice device, int code) {
        Set<Integer>[] states = deviceKeyMap.get(device);
        if (states == null) {
            return false;
        }
        return !states[CURRENT].contains(code) && states[PREVIOUS].contains(code);
    }

    @Override
    public boolean isButtonPressed(InputDevice device, int code) {
        Set<Integer>[] states = deviceButtonMap.get(device);
        if (states == null) {
            return false;
        }
        return states[CURRENT].contains(code) && !states[PREVIOUS].contains(code);
    }

    @Override
    public boolean isButtonHeld(InputDevice device, int code) {
        Set<Integer>[] states = deviceButtonMap.get(device);
        if (states == null) {
            return false;
        }
        return states[CURRENT].contains(code);
    }

    @Override
    public boolean isButtonReleased(InputDevice device, int code) {
        Set<Integer>[] states = deviceButtonMap.get(device);
        if (states == null) {
            return false;
        }
        return !states[CURRENT].contains(code) && states[PREVIOUS].contains(code);
    }

    @Override
    public float getAxis(InputDevice device, int axisCode) {
        if (deviceAxisMap == null) {
            return 0F;
        }
        Map<Integer, Float> map = deviceAxisMap.get(device);
        if (map == null) {
            return 0F;
        }
        return map.getOrDefault(axisCode, 0F);
    }

    @Override
    public float[] getMouse(InputDevice device) {
        return deviceMouseMap.getOrDefault(device, EMPTY);
    }

    @Override
    public float[] getScroll(InputDevice device) {
        return deviceScrollMap.getOrDefault(device, EMPTY);
    }
    //endregion

    //region Mutation (For platform implementation)

    @Override
    public void setKey(InputDevice device, int code, boolean press) {
        //noinspection unchecked
        Set<Integer>[] set = deviceKeyMap.computeIfAbsent(
                device,
                _ -> new Set[] {createKeyIntSet(), createKeyIntSet()}
        );
        if (press) {
            set[CURRENT].add(code);
        } else {
            set[CURRENT].remove(code);
            set[PREVIOUS].add(code);
        }
    }

    @Override
    public void setButton(InputDevice device, int code, boolean press) {
        //noinspection unchecked
        Set<Integer>[] set = deviceButtonMap.computeIfAbsent(
                device,
                _ -> new Set[] {createButtonIntSet(), createButtonIntSet()}
        );
        if (press) {
            set[CURRENT].add(code);
        } else {
            set[CURRENT].remove(code);
            set[PREVIOUS].add(code);
        }
    }

    @Override
    public void setMousePos(InputDevice device, float x, float y) {
        float[] arr = deviceMouseMap.computeIfAbsent(device, _ -> new float[2]);
        arr[0] = x;
        arr[1] = y;
    }

    @Override
    public void setScroll(InputDevice device, float xOffset, float yOffset) {
        float[] arr = deviceScrollMap.computeIfAbsent(device, _ -> new float[2]);
        arr[0] = xOffset;
        arr[1] = yOffset;
    }

    @Override
    public void setAxis(InputDevice device, int axis, float value) {
        if (deviceAxisMap != null) {
            Map<Integer, Float> map = deviceAxisMap.computeIfAbsent(device, _ -> createAxisMap());
            map.put(axis, value);
        }
    }
    //endregion
}
