package ca.nodeengine.quantum.platform.glfw;

import ca.nodeengine.quantum.DefaultInputCode;
import ca.nodeengine.quantum.api.InputEventListener;
import ca.nodeengine.quantum.api.platform.WindowPlatform;
import ca.nodeengine.quantum.event.AxisChangedEvent;
import ca.nodeengine.quantum.event.KeyPressedEvent;
import ca.nodeengine.quantum.event.KeyReleasedEvent;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.system.Callback;

import java.util.*;

/**
 * Adds support for GLFW through LWJGL3
 *
 * @author FX
 */
public class GLFWPlatform implements WindowPlatform {

    // Arbitrary offsets/codes for non-keyboard inputs
    public static final int MOUSE_BUTTON_OFFSET = 1000;
    public static final int AXIS_MOUSE_X = 2000;
    public static final int AXIS_MOUSE_Y = 2001;
    public static final int AXIS_MOUSE_SCROLL_X = 2002;
    public static final int AXIS_MOUSE_SCROLL_Y = 2003;

    private @Nullable InputEventListener listener;
    private final Map<Long, Callback[]> windows = new HashMap<>();

    @Override
    public void initialize(InputEventListener listener) {
        this.listener = listener;
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
    }

    /**
     * Registers a GLFW window to this platform.<br>
     * Callbacks will be installed on the window to capture input events.
     *
     * @param windowHandle the GLFW window handle
     */
    @Override
    public void registerWindow(long windowHandle) {
        if (!windows.containsKey(windowHandle)) {
            windows.put(windowHandle, setupCallbacks(windowHandle));
        }
    }

    /**
     * Un-Registers a GLFW window from this platform.<br>
     * Callbacks for this window will be terminated.
     *
     * @param windowHandle the GLFW window handle
     */
    @Override
    public void unregisterWindow(long windowHandle) {
        Callback[] callbacks = windows.remove(windowHandle);
        if (callbacks != null) {
            for (Callback callback : callbacks) {
                callback.close();
            }
        }
        GLFW.glfwSetKeyCallback(windowHandle, null);
        GLFW.glfwSetMouseButtonCallback(windowHandle, null);
        GLFW.glfwSetCursorPosCallback(windowHandle, null);
        GLFW.glfwSetScrollCallback(windowHandle, null);
    }

    private Callback[] setupCallbacks(long window) {
        Callback[] callbacks = new GLFWKeyCallback[4];
        callbacks[0] = GLFW.glfwSetKeyCallback(window, (w, key, scancode, action, mods) -> {
            if (listener == null || key == GLFW.GLFW_KEY_UNKNOWN) {
                return;
            }
            long timestamp = System.nanoTime();
            if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
                listener.onInputEvent(new KeyPressedEvent(new DefaultInputCode(key, GLFW.glfwGetKeyName(key, scancode)), timestamp));
            } else if (action == GLFW.GLFW_RELEASE) {
                listener.onInputEvent(new KeyReleasedEvent(new DefaultInputCode(key, GLFW.glfwGetKeyName(key, scancode)), timestamp));
            }
        });

        callbacks[1] = GLFW.glfwSetMouseButtonCallback(window, (w, button, action, mods) -> {
            if (listener == null) {
                return;
            }
            long timestamp = System.nanoTime();
            int code = MOUSE_BUTTON_OFFSET + button;
            if (action == GLFW.GLFW_PRESS) {
                listener.onInputEvent(new KeyPressedEvent(new DefaultInputCode(code, "Mouse Button " + button), timestamp));
            } else if (action == GLFW.GLFW_RELEASE) {
                listener.onInputEvent(new KeyReleasedEvent(new DefaultInputCode(code, "Mouse Button " + button), timestamp));
            }
        });

        callbacks[2] = GLFW.glfwSetCursorPosCallback(window, (w, xpos, ypos) -> {
            if (listener == null) {
                return;
            }
            long timestamp = System.nanoTime();
            listener.onInputEvent(new AxisChangedEvent(new DefaultInputCode(AXIS_MOUSE_X, "Mouse X"), (float) xpos, timestamp));
            listener.onInputEvent(new AxisChangedEvent(new DefaultInputCode(AXIS_MOUSE_Y, "Mouse Y"), (float) ypos, timestamp));
        });

        callbacks[3] = GLFW.glfwSetScrollCallback(window, (w, xoffset, yoffset) -> {
            if (listener == null) {
                return;
            }
            long timestamp = System.nanoTime();
            listener.onInputEvent(new AxisChangedEvent(new DefaultInputCode(AXIS_MOUSE_SCROLL_X, "Scroll X"), (float) xoffset, timestamp));
            listener.onInputEvent(new AxisChangedEvent(new DefaultInputCode(AXIS_MOUSE_SCROLL_Y, "Scroll Y"), (float) yoffset, timestamp));
        });
        return callbacks;
    }

    @Override
    public void poll() {
        GLFW.glfwPollEvents();
    }

    @Override
    public void terminate() {
        for (long window : windows.keySet().toArray(new Long[0])) {
            unregisterWindow(window);
        }
        // Note: GLFW.glfwTerminate() is not called here, it should be handled by the application.
    }

    @Override
    public boolean isSupported() {
        try {
            return GLFW.glfwInit();
        } catch (Throwable t) {
            return false;
        }
    }
}
