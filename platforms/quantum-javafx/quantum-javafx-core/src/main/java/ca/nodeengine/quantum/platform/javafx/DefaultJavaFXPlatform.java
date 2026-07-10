package ca.nodeengine.quantum.platform.javafx;

import ca.nodeengine.quantum.InputDevice;
import ca.nodeengine.quantum.Keys;
import ca.nodeengine.quantum.MouseButtons;
import ca.nodeengine.quantum.Settings;
import ca.nodeengine.quantum.event.InputEventType;
import ca.nodeengine.quantum.event.InputListener;
import ca.nodeengine.quantum.event.DefaultInputEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.*;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adds support for JavaFX
 *
 * @author FX
 */
public class DefaultJavaFXPlatform implements JavaFXPlatform {

    private final Map<Object, JavaFXListeners> scenes = new HashMap<>();
    private @Nullable InputListener listener;

    @Override
    public void initialize(InputListener listener) {
        this.listener = listener;
    }

    @Override
    public void registerScene(Object scene) {
        if (scene instanceof Scene fxScene) {
            if (!scenes.containsKey(fxScene)) {
                JavaFXListeners listeners = new JavaFXListeners();
                scenes.put(fxScene, listeners);
                fxScene.addEventHandler(KeyEvent.KEY_PRESSED, listeners.keyPressedHandler);
                fxScene.addEventHandler(KeyEvent.KEY_RELEASED, listeners.keyReleasedHandler);
                fxScene.addEventHandler(MouseEvent.MOUSE_PRESSED, listeners.mousePressedHandler);
                fxScene.addEventHandler(MouseEvent.MOUSE_RELEASED, listeners.mouseReleasedHandler);
                fxScene.addEventHandler(MouseEvent.MOUSE_MOVED, listeners.mouseMovedHandler);
                fxScene.addEventHandler(MouseEvent.MOUSE_DRAGGED, listeners.mouseDraggedHandler);
                fxScene.addEventHandler(ScrollEvent.SCROLL, listeners.scrollHandler);
            }
        }
    }

    @Override
    public void unregisterScene(Object scene) {
        JavaFXListeners listeners = scenes.remove(scene);
        if (listeners != null && scene instanceof Scene fxScene) {
            fxScene.removeEventHandler(KeyEvent.KEY_PRESSED, listeners.keyPressedHandler);
            fxScene.removeEventHandler(KeyEvent.KEY_RELEASED, listeners.keyReleasedHandler);
            fxScene.removeEventHandler(MouseEvent.MOUSE_PRESSED, listeners.mousePressedHandler);
            fxScene.removeEventHandler(MouseEvent.MOUSE_RELEASED, listeners.mouseReleasedHandler);
            fxScene.removeEventHandler(MouseEvent.MOUSE_MOVED, listeners.mouseMovedHandler);
            fxScene.removeEventHandler(MouseEvent.MOUSE_DRAGGED, listeners.mouseDraggedHandler);
            fxScene.removeEventHandler(ScrollEvent.SCROLL, listeners.scrollHandler);
        }
    }

    @Override
    public void update() {
        // JavaFX handles events in its own Application Thread
    }

    @Override
    public void close() {
        for (Object scene : scenes.keySet().toArray()) {
            unregisterScene(scene);
        }
    }

    @Override
    public boolean isSupported() {
        try {
            Class.forName("javafx.scene.Scene");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public Collection<InputDevice> getInputDevices() {
        return List.of(JAVAFX_DEVICE);
    }


    private class JavaFXListeners {
        final EventHandler<KeyEvent> keyPressedHandler;
        final EventHandler<KeyEvent> keyReleasedHandler;
        final EventHandler<MouseEvent> mousePressedHandler;
        final EventHandler<MouseEvent> mouseReleasedHandler;
        final EventHandler<MouseEvent> mouseMovedHandler;
        final EventHandler<MouseEvent> mouseDraggedHandler;
        final EventHandler<ScrollEvent> scrollHandler;

        JavaFXListeners() {
            this.keyPressedHandler = e -> {
                if (listener != null) {
                    listener.onInputEvent(new DefaultInputEvent(
                            JAVAFX_DEVICE, InputEventType.KEY_PRESSED, mapKey(e.getCode()), 0, 0
                    ));
                }
            };

            this.keyReleasedHandler = e -> {
                if (listener != null) {
                    listener.onInputEvent(new DefaultInputEvent(
                            JAVAFX_DEVICE, InputEventType.KEY_RELEASED, mapKey(e.getCode()), 0, 0
                    ));
                }
            };

            this.mousePressedHandler = e -> {
                if (listener != null) {
                    listener.onInputEvent(new DefaultInputEvent(
                            JAVAFX_DEVICE, InputEventType.BUTTON_PRESSED, mapButton(e.getButton()), 0, 0
                    ));
                }
            };

            this.mouseReleasedHandler = e -> {
                if (listener != null) {
                    listener.onInputEvent(new DefaultInputEvent(
                            JAVAFX_DEVICE, InputEventType.BUTTON_RELEASED, mapButton(e.getButton()), 0, 0
                    ));
                }
            };

            this.mouseMovedHandler = e -> {
                if (listener != null) {
                    listener.onInputEvent(new DefaultInputEvent(
                            JAVAFX_DEVICE, InputEventType.MOUSE_CHANGED, 0, (float) e.getX(), (float) e.getY()
                    ));
                }
            };

            this.mouseDraggedHandler = e -> {
                if (listener != null) {
                    listener.onInputEvent(new DefaultInputEvent(
                            JAVAFX_DEVICE, InputEventType.MOUSE_CHANGED, 0, (float) e.getX(), (float) e.getY()
                    ));
                }
            };

            this.scrollHandler = e -> {
                if (listener != null) {
                    listener.onInputEvent(new DefaultInputEvent(
                            JAVAFX_DEVICE, InputEventType.SCROLL, 0, (float) e.getDeltaX(), (float) e.getDeltaY()
                    ));
                }
            };
        }

        /**
         * Maps JavaFx buttons {@link MouseButton} to QuantumInput button numbering
         *
         * @param javaFxButton The JavaFx button.
         * @return The QuantumInput button number.
         */
        private static int mapButton(MouseButton javaFxButton) {
            if (!Settings.STANDARDIZE_MOUSE_BUTTONS) {
                return javaFxButton.ordinal();
            }
            return switch (javaFxButton) {
                case NONE -> MouseButtons.NO_BUTTON;
                case PRIMARY -> MouseButtons.LEFT;
                case MIDDLE -> MouseButtons.MIDDLE;
                case SECONDARY -> MouseButtons.RIGHT;
                case BACK -> MouseButtons.BUTTON_1;
                case FORWARD -> MouseButtons.BUTTON_2;
                default -> javaFxButton.ordinal() - 1; // 5 -> 4, 6 -> 5, etc...
            };
        }

        /**
         * Maps a JavaFX {@link KeyCode} to a canonical {@link Keys} code.
         * <p>
         * JavaFX reports a single {@code SHIFT}/{@code CONTROL}/{@code ALT}/{@code META} for both sides,
         * so those map to the {@code LEFT_*} variants.
         *
         * @param keyCode The JavaFX key code.
         * @return The canonical {@link Keys} code, or {@link Keys#UNKNOWN} if unmapped.
         */
        private static int mapKey(KeyCode keyCode) {
            if (!Settings.STANDARDIZE_KEYS) {
                return keyCode.getCode();
            }
            return switch (keyCode) {
                case SPACE -> Keys.SPACE;
                case QUOTE -> Keys.QUOTE;
                case COMMA -> Keys.COMMA;
                case MINUS -> Keys.MINUS;
                case PERIOD -> Keys.PERIOD;
                case SLASH -> Keys.SLASH;
                case DIGIT0 -> Keys.DIGIT_0;
                case DIGIT1 -> Keys.DIGIT_1;
                case DIGIT2 -> Keys.DIGIT_2;
                case DIGIT3 -> Keys.DIGIT_3;
                case DIGIT4 -> Keys.DIGIT_4;
                case DIGIT5 -> Keys.DIGIT_5;
                case DIGIT6 -> Keys.DIGIT_6;
                case DIGIT7 -> Keys.DIGIT_7;
                case DIGIT8 -> Keys.DIGIT_8;
                case DIGIT9 -> Keys.DIGIT_9;
                case SEMICOLON -> Keys.SEMICOLON;
                case EQUALS -> Keys.EQUAL;
                case A -> Keys.A;
                case B -> Keys.B;
                case C -> Keys.C;
                case D -> Keys.D;
                case E -> Keys.E;
                case F -> Keys.F;
                case G -> Keys.G;
                case H -> Keys.H;
                case I -> Keys.I;
                case J -> Keys.J;
                case K -> Keys.K;
                case L -> Keys.L;
                case M -> Keys.M;
                case N -> Keys.N;
                case O -> Keys.O;
                case P -> Keys.P;
                case Q -> Keys.Q;
                case R -> Keys.R;
                case S -> Keys.S;
                case T -> Keys.T;
                case U -> Keys.U;
                case V -> Keys.V;
                case W -> Keys.W;
                case X -> Keys.X;
                case Y -> Keys.Y;
                case Z -> Keys.Z;
                case OPEN_BRACKET -> Keys.OPEN_BRACKET;
                case BACK_SLASH -> Keys.BACKSLASH;
                case CLOSE_BRACKET -> Keys.CLOSE_BRACKET;
                case BACK_QUOTE -> Keys.GRAVE_ACCENT;
                case ESCAPE -> Keys.ESCAPE;
                case ENTER -> Keys.ENTER;
                case TAB -> Keys.TAB;
                case BACK_SPACE -> Keys.BACKSPACE;
                case INSERT -> Keys.INSERT;
                case DELETE -> Keys.DELETE;
                case RIGHT -> Keys.RIGHT;
                case LEFT -> Keys.LEFT;
                case DOWN -> Keys.DOWN;
                case UP -> Keys.UP;
                case PAGE_UP -> Keys.PAGE_UP;
                case PAGE_DOWN -> Keys.PAGE_DOWN;
                case HOME -> Keys.HOME;
                case END -> Keys.END;
                case CAPS -> Keys.CAPS_LOCK;
                case SCROLL_LOCK -> Keys.SCROLL_LOCK;
                case NUM_LOCK -> Keys.NUM_LOCK;
                case PRINTSCREEN -> Keys.PRINT_SCREEN;
                case PAUSE -> Keys.PAUSE;
                case F1 -> Keys.F1;
                case F2 -> Keys.F2;
                case F3 -> Keys.F3;
                case F4 -> Keys.F4;
                case F5 -> Keys.F5;
                case F6 -> Keys.F6;
                case F7 -> Keys.F7;
                case F8 -> Keys.F8;
                case F9 -> Keys.F9;
                case F10 -> Keys.F10;
                case F11 -> Keys.F11;
                case F12 -> Keys.F12;
                case F13 -> Keys.F13;
                case F14 -> Keys.F14;
                case F15 -> Keys.F15;
                case F16 -> Keys.F16;
                case F17 -> Keys.F17;
                case F18 -> Keys.F18;
                case F19 -> Keys.F19;
                case F20 -> Keys.F20;
                case F21 -> Keys.F21;
                case F22 -> Keys.F22;
                case F23 -> Keys.F23;
                case F24 -> Keys.F24;
                case NUMPAD0 -> Keys.NUMPAD_0;
                case NUMPAD1 -> Keys.NUMPAD_1;
                case NUMPAD2 -> Keys.NUMPAD_2;
                case NUMPAD3 -> Keys.NUMPAD_3;
                case NUMPAD4 -> Keys.NUMPAD_4;
                case NUMPAD5 -> Keys.NUMPAD_5;
                case NUMPAD6 -> Keys.NUMPAD_6;
                case NUMPAD7 -> Keys.NUMPAD_7;
                case NUMPAD8 -> Keys.NUMPAD_8;
                case NUMPAD9 -> Keys.NUMPAD_9;
                case DECIMAL -> Keys.NUMPAD_DECIMAL;
                case DIVIDE -> Keys.NUMPAD_DIVIDE;
                case MULTIPLY -> Keys.NUMPAD_MULTIPLY;
                case SUBTRACT -> Keys.NUMPAD_SUBTRACT;
                case ADD -> Keys.NUMPAD_ADD;
                case KP_UP -> Keys.NUMPAD_UP;
                case KP_DOWN -> Keys.NUMPAD_DOWN;
                case KP_LEFT -> Keys.NUMPAD_LEFT;
                case KP_RIGHT -> Keys.NUMPAD_RIGHT;
                case SHIFT -> Keys.LEFT_SHIFT;
                case CONTROL -> Keys.LEFT_CONTROL;
                case ALT -> Keys.LEFT_ALT;
                case META, WINDOWS, COMMAND -> Keys.LEFT_SUPER;
                case CONTEXT_MENU -> Keys.MENU;
                case KANA -> Keys.KANA;
                case HELP -> Keys.HELP;
                case AGAIN -> Keys.AGAIN;
                case UNDO -> Keys.UNDO;
                case COPY -> Keys.COPY;
                case PASTE -> Keys.PASTE;
                case CUT -> Keys.CUT;
                case FIND -> Keys.FIND;
                case CANCEL -> Keys.CANCEL;
                case CLEAR -> Keys.CLEAR;
                case INVERTED_EXCLAMATION_MARK -> Keys.INVERTED_EXCLAIM;
                case EXCLAMATION_MARK -> Keys.EXCLAIM;
                case QUOTEDBL -> Keys.DOUBLE_QUOTE;
                case NUMBER_SIGN -> Keys.HASH;
                case DOLLAR ->  Keys.DOLLAR;
                case AMPERSAND -> Keys.AMPERSAND;
                case LEFT_PARENTHESIS -> Keys.OPEN_PAREN;
                case RIGHT_PARENTHESIS -> Keys.CLOSE_PAREN;
                case ASTERISK -> Keys.ASTERISK;
                case PLUS -> Keys.PLUS;
                case COLON -> Keys.COLON;
                case LESS -> Keys.LESS;
                case GREATER -> Keys.GREATER;
                case AT -> Keys.AT;
                case CIRCUMFLEX -> Keys.CARET;
                case UNDERSCORE -> Keys.UNDERSCORE;
                default -> Keys.UNKNOWN;
            };
        }
    }
}
