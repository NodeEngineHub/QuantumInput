package ca.nodeengine.quantum.platform.web;

import ca.nodeengine.quantum.Keys;

import java.util.HashMap;
import java.util.Map;

/**
 * Translates browser {@code KeyboardEvent.code} strings (physical-key identifiers, layout independent)
 * into QuantumInput's canonical {@link Keys} codes.
 *
 * @author FX
 */
final class WebKeys {

    private static final Map<String, Integer> CODE_TO_KEY = new HashMap<>(256);

    private WebKeys() {
    }

    /**
     * Translates a {@code KeyboardEvent.code} string to a canonical {@link Keys} code.
     *
     * @param domCode The DOM physical key code (e.g. {@code "KeyA"}, {@code "Digit1"}, {@code "ArrowLeft"}).
     * @return The matching {@link Keys} code, or {@link Keys#UNKNOWN} if there is no mapping.
     */
    static int toQuantum(String domCode) {
        Integer key = CODE_TO_KEY.get(domCode);
        return key != null ? key : Keys.UNKNOWN;
    }

    private static void put(String domCode, int key) {
        CODE_TO_KEY.put(domCode, key);
    }

    static {
        // Individual
        put("NumpadParenLeft", Keys.OPEN_PAREN);
        put("NumpadParenRight", Keys.CLOSE_PAREN);

        // Printable / whitespace
        put("Space", Keys.SPACE);
        put("Quote", Keys.QUOTE);
        put("Comma", Keys.COMMA);
        put("Minus", Keys.MINUS);
        put("Period", Keys.PERIOD);
        put("Slash", Keys.SLASH);

        // Digits (top row)
        put("Digit0", Keys.DIGIT_0);
        put("Digit1", Keys.DIGIT_1);
        put("Digit2", Keys.DIGIT_2);
        put("Digit3", Keys.DIGIT_3);
        put("Digit4", Keys.DIGIT_4);
        put("Digit5", Keys.DIGIT_5);
        put("Digit6", Keys.DIGIT_6);
        put("Digit7", Keys.DIGIT_7);
        put("Digit8", Keys.DIGIT_8);
        put("Digit9", Keys.DIGIT_9);

        put("Semicolon", Keys.SEMICOLON);
        put("Equal", Keys.EQUAL);

        // Letters
        put("KeyA", Keys.A);
        put("KeyB", Keys.B);
        put("KeyC", Keys.C);
        put("KeyD", Keys.D);
        put("KeyE", Keys.E);
        put("KeyF", Keys.F);
        put("KeyG", Keys.G);
        put("KeyH", Keys.H);
        put("KeyI", Keys.I);
        put("KeyJ", Keys.J);
        put("KeyK", Keys.K);
        put("KeyL", Keys.L);
        put("KeyM", Keys.M);
        put("KeyN", Keys.N);
        put("KeyO", Keys.O);
        put("KeyP", Keys.P);
        put("KeyQ", Keys.Q);
        put("KeyR", Keys.R);
        put("KeyS", Keys.S);
        put("KeyT", Keys.T);
        put("KeyU", Keys.U);
        put("KeyV", Keys.V);
        put("KeyW", Keys.W);
        put("KeyX", Keys.X);
        put("KeyY", Keys.Y);
        put("KeyZ", Keys.Z);

        put("BracketLeft", Keys.OPEN_BRACKET);
        put("Backslash", Keys.BACKSLASH);
        put("BracketRight", Keys.CLOSE_BRACKET);
        put("Backquote", Keys.GRAVE_ACCENT);

        // Function / control keys
        put("Escape", Keys.ESCAPE);
        put("Enter", Keys.ENTER);
        put("Tab", Keys.TAB);
        put("Backspace", Keys.BACKSPACE);
        put("Insert", Keys.INSERT);
        put("Delete", Keys.DELETE);
        put("ArrowRight", Keys.RIGHT);
        put("ArrowLeft", Keys.LEFT);
        put("ArrowDown", Keys.DOWN);
        put("ArrowUp", Keys.UP);
        put("PageUp", Keys.PAGE_UP);
        put("PageDown", Keys.PAGE_DOWN);
        put("Home", Keys.HOME);
        put("End", Keys.END);
        put("CapsLock", Keys.CAPS_LOCK);
        put("ScrollLock", Keys.SCROLL_LOCK);
        put("NumLock", Keys.NUM_LOCK);
        put("PrintScreen", Keys.PRINT_SCREEN); // Missing in chromium
        put("Pause", Keys.PAUSE);

        // Function keys F1..F24
        put("F1", Keys.F1);
        put("F2", Keys.F2);
        put("F3", Keys.F3);
        put("F4", Keys.F4);
        put("F5", Keys.F5);
        put("F6", Keys.F6);
        put("F7", Keys.F7);
        put("F8", Keys.F8);
        put("F9", Keys.F9);
        put("F10", Keys.F10);
        put("F11", Keys.F11);
        put("F12", Keys.F12);
        put("F13", Keys.F13);
        put("F14", Keys.F14);
        put("F15", Keys.F15);
        put("F16", Keys.F16);
        put("F17", Keys.F17);
        put("F18", Keys.F18);
        put("F19", Keys.F19);
        put("F20", Keys.F20);
        put("F21", Keys.F21);
        put("F22", Keys.F22);
        put("F23", Keys.F23);
        put("F24", Keys.F24);

        // Keypad
        put("Numpad0", Keys.NUMPAD_0);
        put("Numpad1", Keys.NUMPAD_1);
        put("Numpad2", Keys.NUMPAD_2);
        put("Numpad3", Keys.NUMPAD_3);
        put("Numpad4", Keys.NUMPAD_4);
        put("Numpad5", Keys.NUMPAD_5);
        put("Numpad6", Keys.NUMPAD_6);
        put("Numpad7", Keys.NUMPAD_7);
        put("Numpad8", Keys.NUMPAD_8);
        put("Numpad9", Keys.NUMPAD_9);
        put("NumpadDecimal", Keys.NUMPAD_DECIMAL);
        put("NumpadDivide", Keys.NUMPAD_DIVIDE);
        put("NumpadMultiply", Keys.NUMPAD_MULTIPLY);
        put("NumpadSubtract", Keys.NUMPAD_SUBTRACT);
        put("NumpadAdd", Keys.NUMPAD_ADD);
        put("NumpadEnter", Keys.NUMPAD_ENTER);
        put("NumpadEqual", Keys.NUMPAD_EQUAL);

        // Modifiers
        put("ShiftLeft", Keys.LEFT_SHIFT);
        put("ControlLeft", Keys.LEFT_CONTROL);
        put("AltLeft", Keys.LEFT_ALT);
        put("MetaLeft", Keys.LEFT_SUPER);
        put("ShiftRight", Keys.RIGHT_SHIFT);
        put("ControlRight", Keys.RIGHT_CONTROL);
        put("AltRight", Keys.RIGHT_ALT);
        put("MetaRight", Keys.RIGHT_SUPER);
        put("ContextMenu", Keys.MENU);

        // International / IME
        put("IntlBackslash", Keys.INTL_BACKSLASH);
        put("IntlRo", Keys.INTL_RO);
        put("IntlYen", Keys.INTL_YEN);
        put("Lang1", Keys.LANG_1);
        put("Lang2", Keys.LANG_2);
        put("Convert", Keys.CONVERT);
        put("NonConvert", Keys.NON_CONVERT);
        put("KanaMode", Keys.KANA);

        // Media
        put("MediaPlayPause", Keys.MEDIA_PLAY_PAUSE);
        put("MediaStop", Keys.MEDIA_STOP);
        put("MediaTrackNext", Keys.MEDIA_NEXT);
        put("MediaTrackPrevious", Keys.MEDIA_PREVIOUS);
        put("AudioVolumeUp", Keys.VOLUME_UP);
        put("AudioVolumeDown", Keys.VOLUME_DOWN);
        put("AudioVolumeMute", Keys.VOLUME_MUTE);

        // Browser
        put("BrowserBack", Keys.BROWSER_BACK);
        put("BrowserForward", Keys.BROWSER_FORWARD);
        put("BrowserRefresh", Keys.BROWSER_REFRESH);
        put("BrowserHome", Keys.BROWSER_HOME);
        put("BrowserSearch", Keys.BROWSER_SEARCH);

        // System
        put("Power", Keys.POWER);
        put("Sleep", Keys.SLEEP);
        put("WakeUp", Keys.WAKE);
        put("Hibernate", Keys.HIBERNATE);
        put("Standby", Keys.STANDBY);

        // Application / editing
        put("Help", Keys.HELP);
        put("Select", Keys.SELECT);
        put("Again", Keys.AGAIN);
        put("Undo", Keys.UNDO);
        put("Cut", Keys.CUT);
        put("Copy", Keys.COPY);
        put("Paste", Keys.PASTE);
        put("Find", Keys.FIND);

        // Actions
        put("Cancel", Keys.CANCEL);
        put("Clear", Keys.CLEAR);
        put("Execute", Keys.EXECUTE);
    }
}
