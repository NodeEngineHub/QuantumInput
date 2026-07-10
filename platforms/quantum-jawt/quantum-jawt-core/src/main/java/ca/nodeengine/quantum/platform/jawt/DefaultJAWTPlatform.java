package ca.nodeengine.quantum.platform.jawt;

import ca.nodeengine.quantum.InputDevice;
import ca.nodeengine.quantum.Keys;
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
                        JAWT_DEVICE, InputEventType.KEY_PRESSED, mapKey(e.getKeyLocation(), e.getKeyCode()), 0, 0
                ));
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (listener != null) {
                listener.onInputEvent(new DefaultInputEvent(
                        JAWT_DEVICE, InputEventType.KEY_RELEASED, mapKey(e.getKeyLocation(), e.getKeyCode()), 0, 0
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

        /**
         * Maps an AWT {@link KeyEvent} virtual keycode ({@code VK_*}) to a canonical {@link Keys} code.
         * <p>
         * AWT reports a single {@code VK_SHIFT}/{@code VK_CONTROL}/{@code VK_ALT}/{@code VK_META} for both
         * sides (the side is only in {@code getKeyLocation()}), so we pass the location to determine which
         * one to return. Likewise the numpad Enter shares {@code VK_ENTER} with the main Enter and is told
         * apart by {@code KEY_LOCATION_NUMPAD}.
         *
         * @param keyLocation The AWT virtual location from {@link KeyEvent#getKeyLocation()}.
         * @param awtKeyCode  The AWT virtual keycode from {@link KeyEvent#getKeyCode()}.
         * @return The canonical {@link Keys} code, or {@link Keys#UNKNOWN} if unmapped.
         */
        private static int mapKey(int keyLocation, int awtKeyCode) {
            if (!Settings.STANDARDIZE_KEYS) {
                return awtKeyCode;
            }
            return switch (awtKeyCode) {
                case KeyEvent.VK_INVERTED_EXCLAMATION_MARK -> Keys.INVERTED_EXCLAIM;
                case KeyEvent.VK_EXCLAMATION_MARK -> Keys.EXCLAIM;
                case KeyEvent.VK_QUOTEDBL -> Keys.DOUBLE_QUOTE;
                case KeyEvent.VK_NUMBER_SIGN -> Keys.HASH;
                case KeyEvent.VK_DOLLAR -> Keys.DOLLAR;
                case KeyEvent.VK_AMPERSAND -> Keys.AMPERSAND;
                case KeyEvent.VK_LEFT_PARENTHESIS -> Keys.OPEN_PAREN;
                case KeyEvent.VK_RIGHT_PARENTHESIS -> Keys.CLOSE_PAREN;
                case KeyEvent.VK_ASTERISK -> Keys.ASTERISK;
                case KeyEvent.VK_PLUS -> Keys.PLUS;
                case KeyEvent.VK_COLON -> Keys.COLON;
                case KeyEvent.VK_LESS -> Keys.LESS;
                case KeyEvent.VK_GREATER -> Keys.GREATER;
                case KeyEvent.VK_AT -> Keys.AT;
                case KeyEvent.VK_CIRCUMFLEX -> Keys.CARET;
                case KeyEvent.VK_UNDERSCORE -> Keys.UNDERSCORE;
                case KeyEvent.VK_SPACE -> Keys.SPACE;
                case KeyEvent.VK_QUOTE -> Keys.QUOTE;
                case KeyEvent.VK_COMMA -> Keys.COMMA;
                case KeyEvent.VK_MINUS -> Keys.MINUS;
                case KeyEvent.VK_PERIOD -> Keys.PERIOD;
                case KeyEvent.VK_SLASH -> Keys.SLASH;
                case KeyEvent.VK_0 -> Keys.DIGIT_0;
                case KeyEvent.VK_1 -> Keys.DIGIT_1;
                case KeyEvent.VK_2 -> Keys.DIGIT_2;
                case KeyEvent.VK_3 -> Keys.DIGIT_3;
                case KeyEvent.VK_4 -> Keys.DIGIT_4;
                case KeyEvent.VK_5 -> Keys.DIGIT_5;
                case KeyEvent.VK_6 -> Keys.DIGIT_6;
                case KeyEvent.VK_7 -> Keys.DIGIT_7;
                case KeyEvent.VK_8 -> Keys.DIGIT_8;
                case KeyEvent.VK_9 -> Keys.DIGIT_9;
                case KeyEvent.VK_SEMICOLON -> Keys.SEMICOLON;
                case KeyEvent.VK_EQUALS -> Keys.EQUAL;
                case KeyEvent.VK_A -> Keys.A;
                case KeyEvent.VK_B -> Keys.B;
                case KeyEvent.VK_C -> Keys.C;
                case KeyEvent.VK_D -> Keys.D;
                case KeyEvent.VK_E -> Keys.E;
                case KeyEvent.VK_F -> Keys.F;
                case KeyEvent.VK_G -> Keys.G;
                case KeyEvent.VK_H -> Keys.H;
                case KeyEvent.VK_I -> Keys.I;
                case KeyEvent.VK_J -> Keys.J;
                case KeyEvent.VK_K -> Keys.K;
                case KeyEvent.VK_L -> Keys.L;
                case KeyEvent.VK_M -> Keys.M;
                case KeyEvent.VK_N -> Keys.N;
                case KeyEvent.VK_O -> Keys.O;
                case KeyEvent.VK_P -> Keys.P;
                case KeyEvent.VK_Q -> Keys.Q;
                case KeyEvent.VK_R -> Keys.R;
                case KeyEvent.VK_S -> Keys.S;
                case KeyEvent.VK_T -> Keys.T;
                case KeyEvent.VK_U -> Keys.U;
                case KeyEvent.VK_V -> Keys.V;
                case KeyEvent.VK_W -> Keys.W;
                case KeyEvent.VK_X -> Keys.X;
                case KeyEvent.VK_Y -> Keys.Y;
                case KeyEvent.VK_Z -> Keys.Z;
                case KeyEvent.VK_OPEN_BRACKET -> Keys.OPEN_BRACKET;
                case KeyEvent.VK_BACK_SLASH -> Keys.BACKSLASH;
                case KeyEvent.VK_CLOSE_BRACKET -> Keys.CLOSE_BRACKET;
                case KeyEvent.VK_BACK_QUOTE -> Keys.GRAVE_ACCENT;
                case KeyEvent.VK_ESCAPE -> Keys.ESCAPE;
                case KeyEvent.VK_ENTER ->
                        keyLocation == KeyEvent.KEY_LOCATION_NUMPAD ? Keys.NUMPAD_ENTER : Keys.ENTER;
                case KeyEvent.VK_TAB -> Keys.TAB;
                case KeyEvent.VK_BACK_SPACE -> Keys.BACKSPACE;
                case KeyEvent.VK_INSERT -> Keys.INSERT;
                case KeyEvent.VK_DELETE -> Keys.DELETE;
                case KeyEvent.VK_RIGHT ->
                        keyLocation == KeyEvent.KEY_LOCATION_NUMPAD ? Keys.NUMPAD_RIGHT : Keys.RIGHT;
                case KeyEvent.VK_LEFT ->
                        keyLocation == KeyEvent.KEY_LOCATION_NUMPAD ? Keys.NUMPAD_LEFT : Keys.LEFT;
                case KeyEvent.VK_DOWN ->
                        keyLocation == KeyEvent.KEY_LOCATION_NUMPAD ? Keys.NUMPAD_DOWN : Keys.DOWN;
                case KeyEvent.VK_UP ->
                        keyLocation == KeyEvent.KEY_LOCATION_NUMPAD ? Keys.NUMPAD_UP : Keys.UP;
                case KeyEvent.VK_PAGE_UP -> Keys.PAGE_UP;
                case KeyEvent.VK_PAGE_DOWN -> Keys.PAGE_DOWN;
                case KeyEvent.VK_HOME -> Keys.HOME;
                case KeyEvent.VK_END -> Keys.END;
                case KeyEvent.VK_CAPS_LOCK -> Keys.CAPS_LOCK;
                case KeyEvent.VK_SCROLL_LOCK -> Keys.SCROLL_LOCK;
                case KeyEvent.VK_NUM_LOCK -> Keys.NUM_LOCK;
                case KeyEvent.VK_PRINTSCREEN -> Keys.PRINT_SCREEN;
                case KeyEvent.VK_PAUSE -> Keys.PAUSE;
                case KeyEvent.VK_F1 -> Keys.F1;
                case KeyEvent.VK_F2 -> Keys.F2;
                case KeyEvent.VK_F3 -> Keys.F3;
                case KeyEvent.VK_F4 -> Keys.F4;
                case KeyEvent.VK_F5 -> Keys.F5;
                case KeyEvent.VK_F6 -> Keys.F6;
                case KeyEvent.VK_F7 -> Keys.F7;
                case KeyEvent.VK_F8 -> Keys.F8;
                case KeyEvent.VK_F9 -> Keys.F9;
                case KeyEvent.VK_F10 -> Keys.F10;
                case KeyEvent.VK_F11 -> Keys.F11;
                case KeyEvent.VK_F12 -> Keys.F12;
                case KeyEvent.VK_F13 -> Keys.F13;
                case KeyEvent.VK_F14 -> Keys.F14;
                case KeyEvent.VK_F15 -> Keys.F15;
                case KeyEvent.VK_F16 -> Keys.F16;
                case KeyEvent.VK_F17 -> Keys.F17;
                case KeyEvent.VK_F18 -> Keys.F18;
                case KeyEvent.VK_F19 -> Keys.F19;
                case KeyEvent.VK_F20 -> Keys.F20;
                case KeyEvent.VK_F21 -> Keys.F21;
                case KeyEvent.VK_F22 -> Keys.F22;
                case KeyEvent.VK_F23 -> Keys.F23;
                case KeyEvent.VK_F24 -> Keys.F24;
                case KeyEvent.VK_NUMPAD0 -> Keys.NUMPAD_0;
                case KeyEvent.VK_NUMPAD1 -> Keys.NUMPAD_1;
                case KeyEvent.VK_NUMPAD2 -> Keys.NUMPAD_2;
                case KeyEvent.VK_NUMPAD3 -> Keys.NUMPAD_3;
                case KeyEvent.VK_NUMPAD4 -> Keys.NUMPAD_4;
                case KeyEvent.VK_NUMPAD5 -> Keys.NUMPAD_5;
                case KeyEvent.VK_NUMPAD6 -> Keys.NUMPAD_6;
                case KeyEvent.VK_NUMPAD7 -> Keys.NUMPAD_7;
                case KeyEvent.VK_NUMPAD8 -> Keys.NUMPAD_8;
                case KeyEvent.VK_NUMPAD9 -> Keys.NUMPAD_9;
                case KeyEvent.VK_DECIMAL -> Keys.NUMPAD_DECIMAL;
                case KeyEvent.VK_DIVIDE -> Keys.NUMPAD_DIVIDE;
                case KeyEvent.VK_MULTIPLY -> Keys.NUMPAD_MULTIPLY;
                case KeyEvent.VK_SUBTRACT -> Keys.NUMPAD_SUBTRACT;
                case KeyEvent.VK_ADD -> Keys.NUMPAD_ADD;
                case KeyEvent.VK_SHIFT ->
                        keyLocation == KeyEvent.KEY_LOCATION_RIGHT ? Keys.RIGHT_SHIFT : Keys.LEFT_SHIFT;
                case KeyEvent.VK_CONTROL ->
                        keyLocation == KeyEvent.KEY_LOCATION_RIGHT ? Keys.RIGHT_CONTROL : Keys.LEFT_CONTROL;
                case KeyEvent.VK_ALT ->
                        keyLocation == KeyEvent.KEY_LOCATION_RIGHT ? Keys.RIGHT_ALT : Keys.LEFT_ALT;
                case KeyEvent.VK_META ->
                        keyLocation == KeyEvent.KEY_LOCATION_RIGHT ? Keys.RIGHT_SUPER : Keys.LEFT_SUPER;
                case KeyEvent.VK_CONTEXT_MENU -> Keys.MENU;
                case KeyEvent.VK_KANA -> Keys.KANA;
                case KeyEvent.VK_CONVERT -> Keys.CONVERT;
                case KeyEvent.VK_NONCONVERT -> Keys.NON_CONVERT;
                case KeyEvent.VK_HELP -> Keys.HELP;
                case KeyEvent.VK_AGAIN -> Keys.AGAIN;
                case KeyEvent.VK_UNDO -> Keys.UNDO;
                case KeyEvent.VK_COPY -> Keys.COPY;
                case KeyEvent.VK_PASTE -> Keys.PASTE;
                case KeyEvent.VK_CUT -> Keys.CUT;
                case KeyEvent.VK_FIND -> Keys.FIND;
                case KeyEvent.VK_CANCEL -> Keys.CANCEL;
                case KeyEvent.VK_CLEAR -> Keys.CLEAR;
                default -> Keys.UNKNOWN;
            };
        }
    }
}
