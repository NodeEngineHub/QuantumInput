package ca.nodeengine.quantum.platform.jogl;

import ca.nodeengine.quantum.InputDevice;
import ca.nodeengine.quantum.Keys;
import ca.nodeengine.quantum.MouseButtons;
import ca.nodeengine.quantum.Settings;
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
                                JOGL_DEVICE, InputEventType.KEY_PRESSED, mapKey(e.getKeyCode()), 0, 0
                        ));
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (listener != null && !e.isAutoRepeat()) {
                        listener.onInputEvent(new DefaultInputEvent(
                                JOGL_DEVICE, InputEventType.KEY_RELEASED, mapKey(e.getKeyCode()), 0, 0
                        ));
                    }
                }
            };

            this.mouseListener = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (listener != null) {
                        listener.onInputEvent(new DefaultInputEvent(
                                JOGL_DEVICE, InputEventType.BUTTON_PRESSED, mapButton(e.getButton()), 0, 0
                        ));
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (listener != null) {
                        listener.onInputEvent(new DefaultInputEvent(
                                JOGL_DEVICE, InputEventType.BUTTON_RELEASED, mapButton(e.getButton()), 0, 0
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

        /**
         * Maps JOGL button numbers (0 = no button, 1 = left, 2 = middle, 3 = right) to QuantumInput button numbering
         *
         * @param joglButton The JOGL button number.
         * @return The QuantumInput button number.
         */
        private static int mapButton(int joglButton) {
            if (!Settings.STANDARDIZE_MOUSE_BUTTONS) {
                return joglButton;
            }
            return switch (joglButton) {
                case 0 -> MouseButtons.NO_BUTTON;
                case 1 -> MouseButtons.LEFT;
                case 2 -> MouseButtons.MIDDLE;
                case 3 -> MouseButtons.RIGHT;
                default -> joglButton - 1; // 4 -> 3, 5 -> 4, etc...
            };
        }

        /**
         * Maps a NEWT {@link KeyEvent} virtual keycode ({@code VK_*}) to a canonical {@link Keys} code.
         * <p>
         * NEWT (like AWT) reports a single {@code VK_SHIFT}/{@code VK_CONTROL}/{@code VK_ALT} for both
         * sides, so those map to the {@code LEFT_*} variants.
         *
         * @param newtKeyCode The NEWT virtual keycode from {@link KeyEvent#getKeyCode()}.
         * @return The canonical {@link Keys} code, or {@link Keys#UNKNOWN} if unmapped.
         */
        private static int mapKey(short newtKeyCode) {
            if (!Settings.STANDARDIZE_KEYS) {
                return newtKeyCode;
            }
            return switch (newtKeyCode) {
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
                case KeyEvent.VK_ENTER -> Keys.ENTER;
                case KeyEvent.VK_TAB -> Keys.TAB;
                case KeyEvent.VK_BACK_SPACE -> Keys.BACKSPACE;
                case KeyEvent.VK_INSERT -> Keys.INSERT;
                case KeyEvent.VK_DELETE -> Keys.DELETE;
                case KeyEvent.VK_RIGHT -> Keys.RIGHT;
                case KeyEvent.VK_LEFT -> Keys.LEFT;
                case KeyEvent.VK_DOWN -> Keys.DOWN;
                case KeyEvent.VK_UP -> Keys.UP;
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
                case KeyEvent.VK_SHIFT -> Keys.LEFT_SHIFT;
                case KeyEvent.VK_CONTROL -> Keys.LEFT_CONTROL;
                case KeyEvent.VK_ALT -> Keys.LEFT_ALT;
                case KeyEvent.VK_WINDOWS -> Keys.LEFT_SUPER;
                case KeyEvent.VK_CONTEXT_MENU -> Keys.MENU;
                default -> Keys.UNKNOWN;
            };
        }
    }
}
