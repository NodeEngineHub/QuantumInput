package ca.nodeengine.quantum.platform.glfw;

import ca.nodeengine.quantum.api.event.InputEventType;
import ca.nodeengine.quantum.api.event.InputListener;
import ca.nodeengine.quantum.event.DefaultInputEvent;
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
public class DefaultGLFWPlatform implements GLFWPlatform {

    private final Map<Long, Callback[]> windows = new HashMap<>();
    private @Nullable InputListener listener;

    @Override
    public void initialize(InputListener listener) {
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
            if (action == org.lwjgl.glfw.GLFW.GLFW_PRESS) { //  || action == org.lwjgl.glfw.GLFW.GLFW_REPEAT
                listener.onInputEvent(new DefaultInputEvent(GLFW_DEVICE, InputEventType.KEY_PRESSED, key, 0, 0));
            } else if (action == GLFW.GLFW_RELEASE) {
                listener.onInputEvent(new DefaultInputEvent(GLFW_DEVICE, InputEventType.KEY_RELEASED, key, 0, 0));
                //org.lwjgl.glfw.GLFW.glfwGetKeyName(key, scancode)
            }
        });

        callbacks[1] = GLFW.glfwSetMouseButtonCallback(window, (w, button, action, mods) -> {
            if (listener == null) {
                return;
            }
            if (action == GLFW.GLFW_PRESS) {
                listener.onInputEvent(new DefaultInputEvent(
                        GLFW_DEVICE, InputEventType.BUTTON_PRESSED, button, 0, 0
                ));
            } else if (action == GLFW.GLFW_RELEASE) {
                listener.onInputEvent(new DefaultInputEvent(
                        GLFW_DEVICE, InputEventType.BUTTON_RELEASED, button, 0, 0
                ));
            }
        });

        callbacks[2] = GLFW.glfwSetCursorPosCallback(window, (w, xpos, ypos) -> {
            if (listener == null) {
                return;
            }
            listener.onInputEvent(new DefaultInputEvent(
                    GLFW_DEVICE, InputEventType.MOUSE_CHANGED, 0, (float) xpos, (float) ypos
            ));
        });

        callbacks[3] = GLFW.glfwSetScrollCallback(window, (w, xoffset, yoffset) -> {
            if (listener == null) {
                return;
            }
            listener.onInputEvent(new DefaultInputEvent(
                    GLFW_DEVICE, InputEventType.SCROLL, 0, (float) xoffset, (float) yoffset
            ));
        });
        // TODO: Add Joystick callbacks
        return callbacks;
    }

    @Override
    public void update() {
        GLFW.glfwPollEvents();
    }

    @Override
    public void close() {
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
