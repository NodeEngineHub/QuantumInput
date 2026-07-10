package ca.nodeengine.quantum.platform.jinput;

import ca.nodeengine.quantum.InputDevice;
import ca.nodeengine.quantum.Keys;
import ca.nodeengine.quantum.MouseButtons;
import ca.nodeengine.quantum.Settings;
import ca.nodeengine.quantum.event.InputEventType;
import ca.nodeengine.quantum.event.InputListener;
import ca.nodeengine.quantum.event.DefaultInputEvent;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link JInputPlatform}
 *
 * @author FX
 */
public class DefaultJInputPlatform implements JInputPlatform {

    private @Nullable InputListener listener;
    private Controller[] controllers = new Controller[0];
    private final Map<Controller, InputDevice> deviceMap = new HashMap<>();

    @Override
    public void initialize(InputListener listener) {
        this.listener = listener;
        try {
            this.controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
            for (Controller controller : controllers) {
                deviceMap.put(controller, new JInputDevice(controller));
            }
        } catch (Exception e) {
            // Log or handle? For now, we'll just have no controllers.
            this.controllers = new Controller[0];
        }
    }

    @Override
    public void update() {
        if (listener == null) {
            return;
        }

        for (Controller controller : controllers) {
            if (controller.poll()) {
                EventQueue queue = controller.getEventQueue();
                Event event = new Event();
                while (queue.getNextEvent(event)) {
                    processEvent(controller, event);
                }
            }
        }
    }

    private void processEvent(Controller controller, Event event) {
        if (listener == null) {
            return;
        }

        Component component = event.getComponent();
        Component.Identifier id = component.getIdentifier();
        float value = event.getValue();
        InputDevice device = deviceMap.get(controller);
        if (device == null) {
            return;
        }

        if (id instanceof Component.Identifier.Key keyId) {
            InputEventType type = (value > 0.5f) ? InputEventType.KEY_PRESSED : InputEventType.KEY_RELEASED;
            listener.onInputEvent(new DefaultInputEvent(device, type, mapKey(keyId), value, 0));
        } else if (id instanceof Component.Identifier.Button) {
            InputEventType type = (value > 0.5f) ? InputEventType.BUTTON_PRESSED : InputEventType.BUTTON_RELEASED;
            listener.onInputEvent(new DefaultInputEvent(device, type, mapButton(id.getName()), value, 0));
        } else if (id instanceof Component.Identifier.Axis) {
            if (id == Component.Identifier.Axis.Z && controller.getType() == Controller.Type.MOUSE) {
                listener.onInputEvent(new DefaultInputEvent(device, InputEventType.SCROLL, 0, 0, value));
            } else {
                listener.onInputEvent(new DefaultInputEvent(device, InputEventType.AXIS_CHANGED,
                        getIdentifierCode(id), value, 0));
            }
        }
    }

    private int getIdentifierCode(Component.Identifier id) {
        // Use name hashCode for a relatively stable integer code.
        return id.getName().hashCode();
    }

    /**
     * Maps a JInput Button String name (Left, Middle, Right, Extra_#) to QuantumInput button numbering
     *
     * @param name The JInput button name.
     * @return The QuantumInput button number.
     */
    private static int mapButton(String name) {
        if (!Settings.STANDARDIZE_MOUSE_BUTTONS) {
            return name.hashCode();
        }
        return switch (name) {
            case "Left" -> MouseButtons.LEFT;
            case "Middle" -> MouseButtons.MIDDLE;
            case "Right" -> MouseButtons.RIGHT;
            default -> {
                if (name.startsWith("EXTRA_")) {
                    String number = name.substring(6); // Remove EXTRA_
                    try {
                        yield Integer.parseInt(number) + 2; // 1 -> 3, 2 -> 4, etc...
                    } catch (NumberFormatException ignored) {}
                }
                yield name.hashCode();
            }
        };
    }

    /**
     * Maps a JInput keyboard {@link Component.Identifier.Key} to a canonical {@link Keys} code.
     * <p>
     * JInput exposes keys only up to {@code F15} and does not have a numeric identifier, so the mapping is
     * keyed by the singleton {@link Component.Identifier.Key} instances. Unmapped keys return
     * {@link Keys#UNKNOWN}.
     *
     * @param key The JInput key identifier.
     * @return The canonical {@link Keys} code, or the raw name hash when standardization is disabled.
     */
    private static int mapKey(Component.Identifier.Key key) {
        if (!Settings.STANDARDIZE_KEYS) {
            return key.getName().hashCode();
        }
        return KEY_MAP.getOrDefault(key, Keys.UNKNOWN);
    }

    /** JInput key identifier -> canonical {@link Keys} code. */
    private static final Map<Component.Identifier.Key, Integer> KEY_MAP = new HashMap<>(256);

    private static void putKey(Component.Identifier.Key key, int value) {
        KEY_MAP.put(key, value);
    }

    static {
        // Individual
        putKey(Component.Identifier.Key.COLON, Keys.COLON);
        putKey(Component.Identifier.Key.AT, Keys.AT);
        putKey(Component.Identifier.Key.CIRCUMFLEX, Keys.CARET);

        // Letters
        putKey(Component.Identifier.Key.A, Keys.A);
        putKey(Component.Identifier.Key.B, Keys.B);
        putKey(Component.Identifier.Key.C, Keys.C);
        putKey(Component.Identifier.Key.D, Keys.D);
        putKey(Component.Identifier.Key.E, Keys.E);
        putKey(Component.Identifier.Key.F, Keys.F);
        putKey(Component.Identifier.Key.G, Keys.G);
        putKey(Component.Identifier.Key.H, Keys.H);
        putKey(Component.Identifier.Key.I, Keys.I);
        putKey(Component.Identifier.Key.J, Keys.J);
        putKey(Component.Identifier.Key.K, Keys.K);
        putKey(Component.Identifier.Key.L, Keys.L);
        putKey(Component.Identifier.Key.M, Keys.M);
        putKey(Component.Identifier.Key.N, Keys.N);
        putKey(Component.Identifier.Key.O, Keys.O);
        putKey(Component.Identifier.Key.P, Keys.P);
        putKey(Component.Identifier.Key.Q, Keys.Q);
        putKey(Component.Identifier.Key.R, Keys.R);
        putKey(Component.Identifier.Key.S, Keys.S);
        putKey(Component.Identifier.Key.T, Keys.T);
        putKey(Component.Identifier.Key.U, Keys.U);
        putKey(Component.Identifier.Key.V, Keys.V);
        putKey(Component.Identifier.Key.W, Keys.W);
        putKey(Component.Identifier.Key.X, Keys.X);
        putKey(Component.Identifier.Key.Y, Keys.Y);
        putKey(Component.Identifier.Key.Z, Keys.Z);

        // Digits (top row)
        putKey(Component.Identifier.Key._0, Keys.DIGIT_0);
        putKey(Component.Identifier.Key._1, Keys.DIGIT_1);
        putKey(Component.Identifier.Key._2, Keys.DIGIT_2);
        putKey(Component.Identifier.Key._3, Keys.DIGIT_3);
        putKey(Component.Identifier.Key._4, Keys.DIGIT_4);
        putKey(Component.Identifier.Key._5, Keys.DIGIT_5);
        putKey(Component.Identifier.Key._6, Keys.DIGIT_6);
        putKey(Component.Identifier.Key._7, Keys.DIGIT_7);
        putKey(Component.Identifier.Key._8, Keys.DIGIT_8);
        putKey(Component.Identifier.Key._9, Keys.DIGIT_9);

        // Punctuation
        putKey(Component.Identifier.Key.MINUS, Keys.MINUS);
        putKey(Component.Identifier.Key.EQUALS, Keys.EQUAL);
        putKey(Component.Identifier.Key.LBRACKET, Keys.OPEN_BRACKET);
        putKey(Component.Identifier.Key.RBRACKET, Keys.CLOSE_BRACKET);
        putKey(Component.Identifier.Key.SEMICOLON, Keys.SEMICOLON);
        putKey(Component.Identifier.Key.APOSTROPHE, Keys.QUOTE);
        putKey(Component.Identifier.Key.GRAVE, Keys.GRAVE_ACCENT);
        putKey(Component.Identifier.Key.BACKSLASH, Keys.BACKSLASH);
        putKey(Component.Identifier.Key.COMMA, Keys.COMMA);
        putKey(Component.Identifier.Key.PERIOD, Keys.PERIOD);
        putKey(Component.Identifier.Key.SLASH, Keys.SLASH);
        putKey(Component.Identifier.Key.SPACE, Keys.SPACE);

        // Function / control
        putKey(Component.Identifier.Key.ESCAPE, Keys.ESCAPE);
        putKey(Component.Identifier.Key.BACK, Keys.BACKSPACE);
        putKey(Component.Identifier.Key.TAB, Keys.TAB);
        putKey(Component.Identifier.Key.RETURN, Keys.ENTER);
        putKey(Component.Identifier.Key.CAPITAL, Keys.CAPS_LOCK);
        putKey(Component.Identifier.Key.NUMLOCK, Keys.NUM_LOCK);
        putKey(Component.Identifier.Key.SCROLL, Keys.SCROLL_LOCK);
        putKey(Component.Identifier.Key.SYSRQ, Keys.PRINT_SCREEN);
        putKey(Component.Identifier.Key.PAUSE, Keys.PAUSE);
        putKey(Component.Identifier.Key.INSERT, Keys.INSERT);
        putKey(Component.Identifier.Key.DELETE, Keys.DELETE);
        putKey(Component.Identifier.Key.HOME, Keys.HOME);
        putKey(Component.Identifier.Key.END, Keys.END);
        putKey(Component.Identifier.Key.PAGEUP, Keys.PAGE_UP);
        putKey(Component.Identifier.Key.PAGEDOWN, Keys.PAGE_DOWN);
        putKey(Component.Identifier.Key.UP, Keys.UP);
        putKey(Component.Identifier.Key.DOWN, Keys.DOWN);
        putKey(Component.Identifier.Key.LEFT, Keys.LEFT);
        putKey(Component.Identifier.Key.RIGHT, Keys.RIGHT);
        putKey(Component.Identifier.Key.F1, Keys.F1);
        putKey(Component.Identifier.Key.F2, Keys.F2);
        putKey(Component.Identifier.Key.F3, Keys.F3);
        putKey(Component.Identifier.Key.F4, Keys.F4);
        putKey(Component.Identifier.Key.F5, Keys.F5);
        putKey(Component.Identifier.Key.F6, Keys.F6);
        putKey(Component.Identifier.Key.F7, Keys.F7);
        putKey(Component.Identifier.Key.F8, Keys.F8);
        putKey(Component.Identifier.Key.F9, Keys.F9);
        putKey(Component.Identifier.Key.F10, Keys.F10);
        putKey(Component.Identifier.Key.F11, Keys.F11);
        putKey(Component.Identifier.Key.F12, Keys.F12);
        putKey(Component.Identifier.Key.F13, Keys.F13);
        putKey(Component.Identifier.Key.F14, Keys.F14);
        putKey(Component.Identifier.Key.F15, Keys.F15);

        // Keypad
        putKey(Component.Identifier.Key.NUMPAD0, Keys.NUMPAD_0);
        putKey(Component.Identifier.Key.NUMPAD1, Keys.NUMPAD_1);
        putKey(Component.Identifier.Key.NUMPAD2, Keys.NUMPAD_2);
        putKey(Component.Identifier.Key.NUMPAD3, Keys.NUMPAD_3);
        putKey(Component.Identifier.Key.NUMPAD4, Keys.NUMPAD_4);
        putKey(Component.Identifier.Key.NUMPAD5, Keys.NUMPAD_5);
        putKey(Component.Identifier.Key.NUMPAD6, Keys.NUMPAD_6);
        putKey(Component.Identifier.Key.NUMPAD7, Keys.NUMPAD_7);
        putKey(Component.Identifier.Key.NUMPAD8, Keys.NUMPAD_8);
        putKey(Component.Identifier.Key.NUMPAD9, Keys.NUMPAD_9);
        putKey(Component.Identifier.Key.DECIMAL, Keys.NUMPAD_DECIMAL);
        putKey(Component.Identifier.Key.DIVIDE, Keys.NUMPAD_DIVIDE);
        putKey(Component.Identifier.Key.MULTIPLY, Keys.NUMPAD_MULTIPLY);
        putKey(Component.Identifier.Key.SUBTRACT, Keys.NUMPAD_SUBTRACT);
        putKey(Component.Identifier.Key.ADD, Keys.NUMPAD_ADD);
        putKey(Component.Identifier.Key.NUMPADENTER, Keys.NUMPAD_ENTER);
        putKey(Component.Identifier.Key.NUMPADEQUAL, Keys.NUMPAD_EQUAL);

        // Modifiers
        putKey(Component.Identifier.Key.LSHIFT, Keys.LEFT_SHIFT);
        putKey(Component.Identifier.Key.RSHIFT, Keys.RIGHT_SHIFT);
        putKey(Component.Identifier.Key.LCONTROL, Keys.LEFT_CONTROL);
        putKey(Component.Identifier.Key.RCONTROL, Keys.RIGHT_CONTROL);
        putKey(Component.Identifier.Key.LALT, Keys.LEFT_ALT);
        putKey(Component.Identifier.Key.RALT, Keys.RIGHT_ALT);
        putKey(Component.Identifier.Key.LWIN, Keys.LEFT_SUPER);
        putKey(Component.Identifier.Key.RWIN, Keys.RIGHT_SUPER);
        putKey(Component.Identifier.Key.APPS, Keys.MENU);

        // Extended
        putKey(Component.Identifier.Key.KANA, Keys.KANA);
        putKey(Component.Identifier.Key.CONVERT, Keys.CONVERT);
        putKey(Component.Identifier.Key.NOCONVERT, Keys.NON_CONVERT);
        putKey(Component.Identifier.Key.YEN, Keys.INTL_YEN);
        putKey(Component.Identifier.Key.POWER, Keys.POWER);
        putKey(Component.Identifier.Key.SLEEP, Keys.SLEEP);

        // Actions
        putKey(Component.Identifier.Key.RETURN, Keys.RETURN);
    }

    @Override
    public boolean isSupported() {
        try {
            // Try to access JInput to see if natives are present
            return ControllerEnvironment.getDefaultEnvironment().getControllers() != null;
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public Collection<InputDevice> getInputDevices() {
        return deviceMap.values();
    }

    @Override
    public void close() {
        deviceMap.clear();
        controllers = new Controller[0];
        listener = null;
    }

    private record JInputDevice(Controller controller) implements InputDevice {
        @Override
        public String name() {
            return controller.getName();
        }

        @Override
        public boolean isGlobal() {
            return false;
        }
    }
}
