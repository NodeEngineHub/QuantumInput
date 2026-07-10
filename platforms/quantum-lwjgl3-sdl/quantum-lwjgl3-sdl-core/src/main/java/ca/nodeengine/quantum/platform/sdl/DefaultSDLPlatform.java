package ca.nodeengine.quantum.platform.sdl;

import ca.nodeengine.quantum.InputDevice;
import ca.nodeengine.quantum.Keys;
import ca.nodeengine.quantum.MouseButtons;
import ca.nodeengine.quantum.Settings;
import ca.nodeengine.quantum.event.InputEventType;
import ca.nodeengine.quantum.event.InputListener;
import ca.nodeengine.quantum.event.DefaultInputEvent;
import org.jspecify.annotations.Nullable;
import org.lwjgl.sdl.SDLEvents;
import org.lwjgl.sdl.SDLKeycode;
import org.lwjgl.sdl.SDLInit;
import org.lwjgl.sdl.SDLError;
import org.lwjgl.sdl.SDL_KeyboardEvent;
import org.lwjgl.sdl.SDL_MouseButtonEvent;
import org.lwjgl.sdl.SDL_MouseMotionEvent;
import org.lwjgl.sdl.SDL_MouseWheelEvent;
import org.lwjgl.sdl.SDL_Event;

import java.util.*;

/**
 * Adds support for SDL through LWJGL3
 *
 * @author FX
 */
public class DefaultSDLPlatform implements SDLPlatform {

    private final Set<Integer> windows = new HashSet<>();
    private @Nullable InputListener listener;
    private final SDL_Event event = SDL_Event.create();

    @Override
    public void initialize(InputListener listener) {
        this.listener = listener;
        if (!SDLInit.SDL_Init(SDLInit.SDL_INIT_VIDEO | SDLInit.SDL_INIT_EVENTS)) {
            throw new IllegalStateException("Unable to initialize SDL: " + SDLError.SDL_GetError());
        }
    }

    @Override
    public void registerWindow(long windowHandle) {
        windows.add((int) windowHandle);
    }

    @Override
    public void unregisterWindow(long windowHandle) {
        windows.remove((int) windowHandle);
    }

    @Override
    public void update() {
        while (SDLEvents.SDL_PollEvent(event)) {
            updateSingleEvent();
        }
    }

    public void updateSingleEvent() {
        if (listener == null) {
            return;
        }
        switch (event.type()) {
            case SDLEvents.SDL_EVENT_KEY_DOWN, SDLEvents.SDL_EVENT_KEY_UP -> {
                SDL_KeyboardEvent keyEvent = event.key();
                if (windows.contains(keyEvent.windowID())) {
                    InputEventType type = (event.type() == SDLEvents.SDL_EVENT_KEY_DOWN)
                            ? InputEventType.KEY_PRESSED
                            : InputEventType.KEY_RELEASED;
                    listener.onInputEvent(new DefaultInputEvent(
                            SDL_DEVICE, type, mapKey(keyEvent.key()), 0, 0
                    ));
                }
            }
            case SDLEvents.SDL_EVENT_MOUSE_BUTTON_DOWN, SDLEvents.SDL_EVENT_MOUSE_BUTTON_UP -> {
                SDL_MouseButtonEvent buttonEvent = event.button();
                if (windows.contains(buttonEvent.windowID())) {
                    InputEventType type = (event.type() == SDLEvents.SDL_EVENT_MOUSE_BUTTON_DOWN)
                            ? InputEventType.BUTTON_PRESSED
                            : InputEventType.BUTTON_RELEASED;
                    listener.onInputEvent(new DefaultInputEvent(
                            SDL_DEVICE, type, mapButton(buttonEvent.button()), 0, 0
                    ));
                }
            }
            case SDLEvents.SDL_EVENT_MOUSE_MOTION -> {
                SDL_MouseMotionEvent motionEvent = event.motion();
                if (windows.contains(motionEvent.windowID())) {
                    listener.onInputEvent(new DefaultInputEvent(
                            SDL_DEVICE, InputEventType.MOUSE_CHANGED, 0, motionEvent.x(), motionEvent.y()
                    ));
                }
            }

            case SDLEvents.SDL_EVENT_MOUSE_WHEEL -> {
                SDL_MouseWheelEvent wheelEvent = event.wheel();
                if (windows.contains(wheelEvent.windowID())) {
                    listener.onInputEvent(new DefaultInputEvent(
                            SDL_DEVICE, InputEventType.SCROLL, 0, wheelEvent.x(), wheelEvent.y()
                    ));
                }
            }
        }
    }

    @Override
    public void close() {
        windows.clear();
        // Note: SDL_Quit() is not called here, it should be handled by the application.
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public Collection<InputDevice> getInputDevices() {
        return List.of(SDL_DEVICE);
    }

    public Set<Integer> getWindows() {
        return this.windows;
    }

    public SDL_Event getEvent() {
        return this.event;
    }

    /**
     * Maps a SDL {@code SDL_MouseButtonEvent} (1 = left, 2 = middle, 3 = right) to QuantumInput button numbering
     *
     * @param sdlButton The SDL button number.
     * @return The QuantumInput button number.
     */
    private static int mapButton(int sdlButton) {
        if (!Settings.STANDARDIZE_MOUSE_BUTTONS) {
            return sdlButton;
        }
        return switch (sdlButton) {
            case 1 -> MouseButtons.LEFT;
            case 2 -> MouseButtons.MIDDLE;
            case 3 -> MouseButtons.RIGHT;
            default -> sdlButton - 1; // 4 -> 3, 5 -> 4, etc...
        };
    }

    /**
     * Maps an SDL keycode ({@code SDLK_*} from {@code SDL_KeyboardEvent.key()}) to a canonical
     * {@link Keys} code. Unmapped keycodes return {@link Keys#UNKNOWN}.
     *
     * @param sdlKeycode The SDL keycode.
     * @return The canonical {@link Keys} code, or the raw SDL keycode when standardization is disabled.
     */
    private static int mapKey(int sdlKeycode) {
        if (!Settings.STANDARDIZE_KEYS) {
            return sdlKeycode;
        }
        return SDL_TO_KEY.getOrDefault(sdlKeycode, Keys.UNKNOWN);
    }

    /** SDL keycode -> canonical {@link Keys} code. SDL letters are lowercase ASCII, so the mapping is not identity. */
    private static final Map<Integer, Integer> SDL_TO_KEY = new HashMap<>(256);

    private static void put(int sdlKeycode, int key) {
        SDL_TO_KEY.put(sdlKeycode, key);
    }

    static {
        // Individual
        put(SDLKeycode.SDLK_EXCLAIM, Keys.EXCLAIM);
        put(SDLKeycode.SDLK_DBLAPOSTROPHE, Keys.DOUBLE_QUOTE);
        put(SDLKeycode.SDLK_HASH, Keys.HASH);
        put(SDLKeycode.SDLK_DOLLAR, Keys.DOLLAR);
        put(SDLKeycode.SDLK_PERCENT, Keys.PERCENT);
        put(SDLKeycode.SDLK_AMPERSAND, Keys.AMPERSAND);
        put(SDLKeycode.SDLK_LEFTPAREN, Keys.OPEN_PAREN);
        put(SDLKeycode.SDLK_RIGHTPAREN, Keys.CLOSE_PAREN);
        put(SDLKeycode.SDLK_ASTERISK, Keys.ASTERISK);
        put(SDLKeycode.SDLK_PLUS, Keys.PLUS);
        put(SDLKeycode.SDLK_COLON, Keys.COLON);
        put(SDLKeycode.SDLK_LESS, Keys.LESS);
        put(SDLKeycode.SDLK_GREATER, Keys.GREATER);
        put(SDLKeycode.SDLK_QUESTION, Keys.QUESTION);
        put(SDLKeycode.SDLK_AT, Keys.AT);
        put(SDLKeycode.SDLK_CARET, Keys.CARET);
        put(SDLKeycode.SDLK_UNDERSCORE, Keys.UNDERSCORE);
        put(SDLKeycode.SDLK_PIPE, Keys.PIPE);
        put(SDLKeycode.SDLK_TILDE, Keys.TILDE);

        // Printable
        put(SDLKeycode.SDLK_SPACE, Keys.SPACE);
        put(SDLKeycode.SDLK_APOSTROPHE, Keys.QUOTE);
        put(SDLKeycode.SDLK_COMMA, Keys.COMMA);
        put(SDLKeycode.SDLK_MINUS, Keys.MINUS);
        put(SDLKeycode.SDLK_PERIOD, Keys.PERIOD);
        put(SDLKeycode.SDLK_SLASH, Keys.SLASH);
        put(SDLKeycode.SDLK_0, Keys.DIGIT_0);
        put(SDLKeycode.SDLK_1, Keys.DIGIT_1);
        put(SDLKeycode.SDLK_2, Keys.DIGIT_2);
        put(SDLKeycode.SDLK_3, Keys.DIGIT_3);
        put(SDLKeycode.SDLK_4, Keys.DIGIT_4);
        put(SDLKeycode.SDLK_5, Keys.DIGIT_5);
        put(SDLKeycode.SDLK_6, Keys.DIGIT_6);
        put(SDLKeycode.SDLK_7, Keys.DIGIT_7);
        put(SDLKeycode.SDLK_8, Keys.DIGIT_8);
        put(SDLKeycode.SDLK_9, Keys.DIGIT_9);
        put(SDLKeycode.SDLK_SEMICOLON, Keys.SEMICOLON);
        put(SDLKeycode.SDLK_EQUALS, Keys.EQUAL);
        put(SDLKeycode.SDLK_A, Keys.A);
        put(SDLKeycode.SDLK_B, Keys.B);
        put(SDLKeycode.SDLK_C, Keys.C);
        put(SDLKeycode.SDLK_D, Keys.D);
        put(SDLKeycode.SDLK_E, Keys.E);
        put(SDLKeycode.SDLK_F, Keys.F);
        put(SDLKeycode.SDLK_G, Keys.G);
        put(SDLKeycode.SDLK_H, Keys.H);
        put(SDLKeycode.SDLK_I, Keys.I);
        put(SDLKeycode.SDLK_J, Keys.J);
        put(SDLKeycode.SDLK_K, Keys.K);
        put(SDLKeycode.SDLK_L, Keys.L);
        put(SDLKeycode.SDLK_M, Keys.M);
        put(SDLKeycode.SDLK_N, Keys.N);
        put(SDLKeycode.SDLK_O, Keys.O);
        put(SDLKeycode.SDLK_P, Keys.P);
        put(SDLKeycode.SDLK_Q, Keys.Q);
        put(SDLKeycode.SDLK_R, Keys.R);
        put(SDLKeycode.SDLK_S, Keys.S);
        put(SDLKeycode.SDLK_T, Keys.T);
        put(SDLKeycode.SDLK_U, Keys.U);
        put(SDLKeycode.SDLK_V, Keys.V);
        put(SDLKeycode.SDLK_W, Keys.W);
        put(SDLKeycode.SDLK_X, Keys.X);
        put(SDLKeycode.SDLK_Y, Keys.Y);
        put(SDLKeycode.SDLK_Z, Keys.Z);
        put(SDLKeycode.SDLK_LEFTBRACKET, Keys.OPEN_BRACKET);
        put(SDLKeycode.SDLK_BACKSLASH, Keys.BACKSLASH);
        put(SDLKeycode.SDLK_RIGHTBRACKET, Keys.CLOSE_BRACKET);
        put(SDLKeycode.SDLK_GRAVE, Keys.GRAVE_ACCENT);

        // Function / control
        put(SDLKeycode.SDLK_ESCAPE, Keys.ESCAPE);
        put(SDLKeycode.SDLK_RETURN, Keys.ENTER);
        put(SDLKeycode.SDLK_TAB, Keys.TAB);
        put(SDLKeycode.SDLK_BACKSPACE, Keys.BACKSPACE);
        put(SDLKeycode.SDLK_INSERT, Keys.INSERT);
        put(SDLKeycode.SDLK_DELETE, Keys.DELETE);
        put(SDLKeycode.SDLK_RIGHT, Keys.RIGHT);
        put(SDLKeycode.SDLK_LEFT, Keys.LEFT);
        put(SDLKeycode.SDLK_DOWN, Keys.DOWN);
        put(SDLKeycode.SDLK_UP, Keys.UP);
        put(SDLKeycode.SDLK_PAGEUP, Keys.PAGE_UP);
        put(SDLKeycode.SDLK_PAGEDOWN, Keys.PAGE_DOWN);
        put(SDLKeycode.SDLK_HOME, Keys.HOME);
        put(SDLKeycode.SDLK_END, Keys.END);
        put(SDLKeycode.SDLK_CAPSLOCK, Keys.CAPS_LOCK);
        put(SDLKeycode.SDLK_SCROLLLOCK, Keys.SCROLL_LOCK);
        put(SDLKeycode.SDLK_NUMLOCKCLEAR, Keys.NUM_LOCK);
        put(SDLKeycode.SDLK_PRINTSCREEN, Keys.PRINT_SCREEN);
        put(SDLKeycode.SDLK_PAUSE, Keys.PAUSE);
        put(SDLKeycode.SDLK_F1, Keys.F1);
        put(SDLKeycode.SDLK_F2, Keys.F2);
        put(SDLKeycode.SDLK_F3, Keys.F3);
        put(SDLKeycode.SDLK_F4, Keys.F4);
        put(SDLKeycode.SDLK_F5, Keys.F5);
        put(SDLKeycode.SDLK_F6, Keys.F6);
        put(SDLKeycode.SDLK_F7, Keys.F7);
        put(SDLKeycode.SDLK_F8, Keys.F8);
        put(SDLKeycode.SDLK_F9, Keys.F9);
        put(SDLKeycode.SDLK_F10, Keys.F10);
        put(SDLKeycode.SDLK_F11, Keys.F11);
        put(SDLKeycode.SDLK_F12, Keys.F12);
        put(SDLKeycode.SDLK_F13, Keys.F13);
        put(SDLKeycode.SDLK_F14, Keys.F14);
        put(SDLKeycode.SDLK_F15, Keys.F15);
        put(SDLKeycode.SDLK_F16, Keys.F16);
        put(SDLKeycode.SDLK_F17, Keys.F17);
        put(SDLKeycode.SDLK_F18, Keys.F18);
        put(SDLKeycode.SDLK_F19, Keys.F19);
        put(SDLKeycode.SDLK_F20, Keys.F20);
        put(SDLKeycode.SDLK_F21, Keys.F21);
        put(SDLKeycode.SDLK_F22, Keys.F22);
        put(SDLKeycode.SDLK_F23, Keys.F23);
        put(SDLKeycode.SDLK_F24, Keys.F24);

        // Keypad
        put(SDLKeycode.SDLK_KP_0, Keys.NUMPAD_0);
        put(SDLKeycode.SDLK_KP_1, Keys.NUMPAD_1);
        put(SDLKeycode.SDLK_KP_2, Keys.NUMPAD_2);
        put(SDLKeycode.SDLK_KP_3, Keys.NUMPAD_3);
        put(SDLKeycode.SDLK_KP_4, Keys.NUMPAD_4);
        put(SDLKeycode.SDLK_KP_5, Keys.NUMPAD_5);
        put(SDLKeycode.SDLK_KP_6, Keys.NUMPAD_6);
        put(SDLKeycode.SDLK_KP_7, Keys.NUMPAD_7);
        put(SDLKeycode.SDLK_KP_8, Keys.NUMPAD_8);
        put(SDLKeycode.SDLK_KP_9, Keys.NUMPAD_9);
        put(SDLKeycode.SDLK_KP_PERIOD, Keys.NUMPAD_DECIMAL);
        put(SDLKeycode.SDLK_KP_DIVIDE, Keys.NUMPAD_DIVIDE);
        put(SDLKeycode.SDLK_KP_MULTIPLY, Keys.NUMPAD_MULTIPLY);
        put(SDLKeycode.SDLK_KP_MINUS, Keys.NUMPAD_SUBTRACT);
        put(SDLKeycode.SDLK_KP_PLUS, Keys.NUMPAD_ADD);
        put(SDLKeycode.SDLK_KP_ENTER, Keys.NUMPAD_ENTER);
        put(SDLKeycode.SDLK_KP_EQUALS, Keys.NUMPAD_EQUAL);

        // Modifiers
        put(SDLKeycode.SDLK_LSHIFT, Keys.LEFT_SHIFT);
        put(SDLKeycode.SDLK_LCTRL, Keys.LEFT_CONTROL);
        put(SDLKeycode.SDLK_LALT, Keys.LEFT_ALT);
        put(SDLKeycode.SDLK_LGUI, Keys.LEFT_SUPER);
        put(SDLKeycode.SDLK_RSHIFT, Keys.RIGHT_SHIFT);
        put(SDLKeycode.SDLK_RCTRL, Keys.RIGHT_CONTROL);
        put(SDLKeycode.SDLK_RALT, Keys.RIGHT_ALT);
        put(SDLKeycode.SDLK_RGUI, Keys.RIGHT_SUPER);
        put(SDLKeycode.SDLK_APPLICATION, Keys.MENU);

        // Extended
        put(SDLKeycode.SDLK_MEDIA_PLAY_PAUSE, Keys.MEDIA_PLAY_PAUSE);
        put(SDLKeycode.SDLK_MEDIA_STOP, Keys.MEDIA_STOP);
        put(SDLKeycode.SDLK_MEDIA_NEXT_TRACK, Keys.MEDIA_NEXT);
        put(SDLKeycode.SDLK_MEDIA_PREVIOUS_TRACK, Keys.MEDIA_PREVIOUS);
        put(SDLKeycode.SDLK_VOLUMEUP, Keys.VOLUME_UP);
        put(SDLKeycode.SDLK_VOLUMEDOWN, Keys.VOLUME_DOWN);
        put(SDLKeycode.SDLK_MUTE, Keys.VOLUME_MUTE);
        put(SDLKeycode.SDLK_AC_BACK, Keys.BROWSER_BACK);
        put(SDLKeycode.SDLK_AC_FORWARD, Keys.BROWSER_FORWARD);
        put(SDLKeycode.SDLK_AC_REFRESH, Keys.BROWSER_REFRESH);
        put(SDLKeycode.SDLK_AC_HOME, Keys.BROWSER_HOME);
        put(SDLKeycode.SDLK_AC_SEARCH, Keys.BROWSER_SEARCH);
        put(SDLKeycode.SDLK_POWER, Keys.POWER);
        put(SDLKeycode.SDLK_SLEEP, Keys.SLEEP);
        put(SDLKeycode.SDLK_WAKE, Keys.WAKE);
        put(SDLKeycode.SDLK_HELP, Keys.HELP);
        put(SDLKeycode.SDLK_SELECT, Keys.SELECT);
        put(SDLKeycode.SDLK_AGAIN, Keys.AGAIN);
        put(SDLKeycode.SDLK_UNDO, Keys.UNDO);
        put(SDLKeycode.SDLK_CUT, Keys.CUT);
        put(SDLKeycode.SDLK_COPY, Keys.COPY);
        put(SDLKeycode.SDLK_PASTE, Keys.PASTE);
        put(SDLKeycode.SDLK_FIND, Keys.FIND);

        // Actions
        put(SDLKeycode.SDLK_CANCEL, Keys.CANCEL);
        put(SDLKeycode.SDLK_CLEAR, Keys.CLEAR);
        put(SDLKeycode.SDLK_EXECUTE, Keys.EXECUTE);
        put(SDLKeycode.SDLK_RETURN, Keys.RETURN);
    }
}
