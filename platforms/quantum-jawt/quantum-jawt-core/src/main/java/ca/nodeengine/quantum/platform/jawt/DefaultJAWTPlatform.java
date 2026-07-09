package ca.nodeengine.quantum.platform.jawt;

import ca.nodeengine.quantum.InputDevice;
import ca.nodeengine.quantum.MouseButtons;
import ca.nodeengine.quantum.Settings;
import ca.nodeengine.quantum.event.InputEventType;
import ca.nodeengine.quantum.event.InputListener;
import ca.nodeengine.quantum.event.DefaultInputEvent;
import org.jspecify.annotations.Nullable;

import java.awt.Component;
import java.awt.event.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adds support for JAWT (AWT/Swing)
 *
 * @author FX
 */
public class DefaultJAWTPlatform implements JAWTPlatform {

    private final Map<Component, JAWTListeners> components = new HashMap<>();
    private @Nullable InputListener listener;

    @Override
    public void initialize(InputListener listener) {
        this.listener = listener;
    }

    @Override
    public void registerComponent(Component component) {
        if (!components.containsKey(component)) {
            JAWTListeners listeners = new JAWTListeners();
            components.put(component, listeners);
            component.addKeyListener(listeners);
            component.addMouseListener(listeners);
            component.addMouseMotionListener(listeners);
            component.addMouseWheelListener(listeners);
        }
    }

    @Override
    public void unregisterComponent(Component component) {
        JAWTListeners listeners = components.remove(component);
        if (listeners != null) {
            component.removeKeyListener(listeners);
            component.removeMouseListener(listeners);
            component.removeMouseMotionListener(listeners);
            component.removeMouseWheelListener(listeners);
        }
    }

    @Override
    public void update() {
        // AWT handles events in its own Event Dispatch Thread
    }

    @Override
    public void close() {
        for (Component component : components.keySet().toArray(new Component[0])) {
            unregisterComponent(component);
        }
    }

    @Override
    public boolean isSupported() {
        try {
            Class.forName("java.awt.Component");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public Collection<InputDevice> getInputDevices() {
        return List.of(JAWT_DEVICE);
    }


    private class JAWTListeners implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

        @Override
        public void keyTyped(KeyEvent e) {
            // Not used
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (listener != null) {
                listener.onInputEvent(new DefaultInputEvent(
                        JAWT_DEVICE, InputEventType.KEY_PRESSED, e.getKeyCode(), 0, 0
                ));
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (listener != null) {
                listener.onInputEvent(new DefaultInputEvent(
                        JAWT_DEVICE, InputEventType.KEY_RELEASED, e.getKeyCode(), 0, 0
                ));
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // Not used
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (listener != null) {
                listener.onInputEvent(new DefaultInputEvent(
                        JAWT_DEVICE, InputEventType.BUTTON_PRESSED, mapButton(e.getButton()), 0, 0
                ));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (listener != null) {
                listener.onInputEvent(new DefaultInputEvent(
                        JAWT_DEVICE, InputEventType.BUTTON_RELEASED, mapButton(e.getButton()), 0, 0
                ));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // Not used
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // Not used
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (listener != null) {
                listener.onInputEvent(new DefaultInputEvent(
                        JAWT_DEVICE, InputEventType.MOUSE_CHANGED, 0, (float) e.getX(), (float) e.getY()
                ));
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (listener != null) {
                listener.onInputEvent(new DefaultInputEvent(
                        JAWT_DEVICE, InputEventType.MOUSE_CHANGED, 0, (float) e.getX(), (float) e.getY()
                ));
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (listener != null) {
                // AWT mouse wheel rotation: negative is up/away, positive is down/towards
                // We'll pass it as deltaY
                listener.onInputEvent(new DefaultInputEvent(
                        JAWT_DEVICE, InputEventType.SCROLL, 0, 0, (float) -e.getPreciseWheelRotation()
                ));
            }
        }

        /**
         * Maps JAWT button numbers (0 = no button, 1 = left, 2 = middle, 3 = right) to QuantumInput button numbering
         *
         * @param jawtButton The JAWT button number.
         * @return The QuantumInput button number.
         */
        private static int mapButton(int jawtButton) {
            if (!Settings.STANDARDIZE_MOUSE_BUTTONS) {
                return jawtButton;
            }
            return switch (jawtButton) {
                case 0 -> MouseButtons.NO_BUTTON;
                case 1 -> MouseButtons.LEFT;
                case 2 -> MouseButtons.MIDDLE;
                case 3 -> MouseButtons.RIGHT;
                default -> jawtButton - 1; // 4 -> 3, 5 -> 4, etc...
            };
        }
    }
}
