package ca.nodeengine.quantum.platform.jogl;

import ca.nodeengine.quantum.InputDevice;
import ca.nodeengine.quantum.event.InputEventType;
import ca.nodeengine.quantum.event.InputListener;
import ca.nodeengine.quantum.event.DefaultInputEvent;
import com.jogamp.newt.Window;
import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adds support for JOGL
 *
 * @author FX
 */
public class DefaultJOGLPlatform implements JOGLPlatform {

    private final Map<Object, JOGLListeners> windows = new HashMap<>();
    private @Nullable InputListener listener;

    @Override
    public void initialize(InputListener listener) {
        this.listener = listener;
    }

    @Override
    public void registerWindow(Object window) {
        if (window instanceof Window newtWindow) {
            if (!windows.containsKey(newtWindow)) {
                JOGLListeners listeners = new JOGLListeners();
                windows.put(newtWindow, listeners);
                newtWindow.addKeyListener(listeners.keyListener);
                newtWindow.addMouseListener(listeners.mouseListener);
            }
        }
    }

    @Override
    public void unregisterWindow(Object window) {
        JOGLListeners listeners = windows.remove(window);
        if (listeners != null && window instanceof Window newtWindow) {
            newtWindow.removeKeyListener(listeners.keyListener);
            newtWindow.removeMouseListener(listeners.mouseListener);
        }
    }

    @Override
    public void update() {
        // JOGL handles events in its own thread or via window.display() / animator
    }

    @Override
    public void close() {
        for (Object window : windows.keySet().toArray()) {
            unregisterWindow(window);
        }
    }

    @Override
    public boolean isSupported() {
        try {
            Class.forName("com.jogamp.newt.Window");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public Collection<InputDevice> getInputDevices() {
        return List.of(JOGL_DEVICE);
    }

    private class JOGLListeners {
        final KeyAdapter keyListener;
        final MouseAdapter mouseListener;

        JOGLListeners() {
            this.keyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (listener != null && !e.isAutoRepeat()) {
                        listener.onInputEvent(new DefaultInputEvent(
                                JOGL_DEVICE, InputEventType.KEY_PRESSED, e.getKeyCode(), 0, 0
                        ));
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (listener != null && !e.isAutoRepeat()) {
                        listener.onInputEvent(new DefaultInputEvent(
                                JOGL_DEVICE, InputEventType.KEY_RELEASED, e.getKeyCode(), 0, 0
                        ));
                    }
                }
            };

            this.mouseListener = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (listener != null) {
                        listener.onInputEvent(new DefaultInputEvent(
                                JOGL_DEVICE, InputEventType.BUTTON_PRESSED, e.getButton(), 0, 0
                        ));
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (listener != null) {
                        listener.onInputEvent(new DefaultInputEvent(
                                JOGL_DEVICE, InputEventType.BUTTON_RELEASED, e.getButton(), 0, 0
                        ));
                    }
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    if (listener != null) {
                        listener.onInputEvent(new DefaultInputEvent(
                                JOGL_DEVICE, InputEventType.MOUSE_CHANGED, 0, e.getX(), e.getY()
                        ));
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (listener != null) {
                        listener.onInputEvent(new DefaultInputEvent(
                                JOGL_DEVICE, InputEventType.MOUSE_CHANGED, 0, e.getX(), e.getY()
                        ));
                    }
                }

                @Override
                public void mouseWheelMoved(MouseEvent e) {
                    if (listener != null) {
                        float[] rotation = e.getRotation();
                        // rotation[0] is horizontal, rotation[1] is vertical
                        listener.onInputEvent(new DefaultInputEvent(
                                JOGL_DEVICE, InputEventType.SCROLL, 0, rotation[0], rotation[1]
                        ));
                    }
                }
            };
        }
    }
}
