package ca.nodeengine.quantum;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.InputProcessor;
import ca.nodeengine.quantum.api.state.PerDeviceInputState;
import ca.nodeengine.quantum.state.MutableInputState;
import org.jspecify.annotations.Nullable;

import java.util.*;

/**
 * The default implementation of {@link PerDeviceInputState}.
 * <p>
 * This class also implements {@link MutableInputState} to allow updates from {@link InputProcessor}.
 * It stores input states for each {@link InputDevice} separately.
 *
 * @author FX
 */
public class DefaultPerDeviceInputState implements PerDeviceInputState, MutableInputState {

    /** Index for current state in the state array. */
    protected static final int CURRENT = 0;
    /** Index for state in the previous frame in the state array. */
    protected static final int WAS_DOWN = 1;
    /** Index for just released state in the state array. */
    protected static final int JUST_RELEASED = 2;

    /** Default empty mouse/scroll value. */
    protected static final float[] EMPTY = new float[] {0, 0};

    /** Map of key states per device. Each device has an array of three sets: [current, was_down, just_released]. */
    protected final Map<InputDevice, Set<Integer>[]> deviceKeyMap = createDeviceKeyMap();
    /** Map of mouse button states per device. Each device has an array of three sets: [current, was_down, just_released]. */
    protected final Map<InputDevice, Set<Integer>[]> deviceButtonMap = createDeviceButtonMap();
    /** Map of analog axis values per device. */
    protected final @Nullable Map<InputDevice, Map<Integer, Float>> deviceAxisMap = createDeviceAxisMap();
    /** Map of mouse positions per device. */
    protected final Map<InputDevice, float[]> deviceMouseMap = createMouseAndScrollMap();
    /** Map of mouse scroll velocities per device. */
    protected final Map<InputDevice, float[]> deviceScrollMap = createMouseAndScrollMap();

    //region Default creators

    /**
     * Creates the map used to store key states per device.
     * <p>
     * Override this to provide a faster map implementation.
     *
     * @return A map for device key states.
     */
    protected Map<InputDevice, Set<Integer>[]> createDeviceKeyMap() {
        return new HashMap<>();
    }

    /**
     * Creates the map used to store mouse button states per device.
     * <p>
     * Override this to provide a faster map implementation.
     *
     * @return A map for device button states.
     */
    protected Map<InputDevice, Set<Integer>[]> createDeviceButtonMap() {
        return new HashMap<>();
    }

    /**
     * Creates a set for storing key codes.
     * <p>
     * Override this to provide a faster set implementation.
     *
     * @return A set for key codes.
     */
    protected Set<Integer> createKeyIntSet() {
        return new HashSet<>();
    }

    /**
     * Creates a set for storing mouse button codes.
     * <p>
     * Override this to provide a faster set implementation.
     *
     * @return A set for button codes.
     */
    protected Set<Integer> createButtonIntSet() {
        return new HashSet<>();
    }

    /**
     * Creates the map used to store axis values per device.
     * <p>
     * Override this to return {@code null} if axes are not needed, or to provide a faster map implementation.
     *
     * @return A map for device axis values.
     */
    protected @Nullable Map<InputDevice, Map<Integer, Float>> createDeviceAxisMap() {
        return new HashMap<>();
    }

    /**
     * Creates a map for storing axis codes and values.
     * <p>
     * Override this to provide a faster map implementation.
     *
     * @return A map for axis values.
     */
    protected Map<Integer, Float> createAxisMap() {
        return new HashMap<>();
    }

    /**
     * Creates a map for storing mouse positions or scroll velocities per device.
     * <p>
     * This is called twice: once for the mouse map and once for the scroll map.
     *
     * @return A map for device vector data.
     */
    protected Map<InputDevice, float[]> createMouseAndScrollMap() {
        return new HashMap<>();
    }
    //endregion

    @Override
    public void update() {
        for (Map.Entry<InputDevice, Set<Integer>[]> entry : deviceKeyMap.entrySet()) {
            Set<Integer>[] states = entry.getValue();
            states[WAS_DOWN].clear();
            states[WAS_DOWN].addAll(states[CURRENT]);
            states[JUST_RELEASED].clear();
        }
        for (Map.Entry<InputDevice, Set<Integer>[]> entry : deviceButtonMap.entrySet()) {
            Set<Integer>[] states = entry.getValue();
            states[WAS_DOWN].clear();
            states[WAS_DOWN].addAll(states[CURRENT]);
            states[JUST_RELEASED].clear();
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
        return states[CURRENT].contains(code) && !states[WAS_DOWN].contains(code);
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
        return states[JUST_RELEASED].contains(code);
    }

    @Override
    public boolean isButtonPressed(InputDevice device, int code) {
        Set<Integer>[] states = deviceButtonMap.get(device);
        if (states == null) {
            return false;
        }
        return states[CURRENT].contains(code) && !states[WAS_DOWN].contains(code);
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
        return states[JUST_RELEASED].contains(code);
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
                _ -> new Set[] {createKeyIntSet(), createKeyIntSet(), createKeyIntSet()}
        );
        if (press) {
            set[CURRENT].add(code);
        } else {
            set[CURRENT].remove(code);
            set[JUST_RELEASED].add(code);
        }
    }

    @Override
    public void setButton(InputDevice device, int code, boolean press) {
        //noinspection unchecked
        Set<Integer>[] set = deviceButtonMap.computeIfAbsent(
                device,
                _ -> new Set[] {createButtonIntSet(), createButtonIntSet(), createButtonIntSet()}
        );
        if (press) {
            set[CURRENT].add(code);
        } else {
            set[CURRENT].remove(code);
            set[JUST_RELEASED].add(code);
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
