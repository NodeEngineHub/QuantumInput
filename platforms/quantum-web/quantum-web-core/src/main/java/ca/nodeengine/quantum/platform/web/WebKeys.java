package ca.nodeengine.quantum.platform.web;

import java.util.HashMap;
import java.util.Map;

/**
 * Translates browser {@code KeyboardEvent.code} strings (physical-key identifiers, layout independent)
 * into the GLFW keycode integers used by the reference LWJGL3 backend.
 * <p>
 * QuantumInput does not define its own keycode table; the GLFW platform reports raw GLFW keycodes. To
 * keep applications seeing consistent codes across the desktop and web backends, this platform reports
 * the same integer values GLFW does. The constants below are the standard GLFW3 keycodes (from
 * {@code GLFW/glfw3.h}); only the numeric values are used, so nothing here pulls in a native dependency.
 * <p>
 * Coverage: the printable ASCII block (letters, digits, common punctuation), the function keys
 * F1-F25, the numeric keypad, and the standard navigation / modifier / editing keys. DOM codes
 * without a GLFW analog map to {@link #GLFW_KEY_UNKNOWN} ({@code -1}).
 *
 * @author FX
 */
final class WebKeys {

    /** GLFW {@code GLFW_KEY_UNKNOWN}. */
    static final int GLFW_KEY_UNKNOWN = -1;

    private static final Map<String, Integer> CODE_TO_GLFW = new HashMap<>(256);

    private WebKeys() {
    }

    /**
     * Translates a {@code KeyboardEvent.code} string to a GLFW keycode.
     *
     * @param domCode The DOM physical key code (e.g. {@code "KeyA"}, {@code "Digit1"}, {@code "ArrowLeft"}).
     * @return The matching GLFW keycode, or {@link #GLFW_KEY_UNKNOWN} if there is no mapping.
     */
    static int toGlfw(String domCode) {
        Integer glfw = CODE_TO_GLFW.get(domCode);
        return glfw != null ? glfw : GLFW_KEY_UNKNOWN;
    }

    private static void put(String domCode, int glfw) {
        CODE_TO_GLFW.put(domCode, glfw);
    }

    static {
        // Printable / whitespace
        put("Space", 32);
        put("Quote", 39);          // GLFW_KEY_APOSTROPHE
        put("Comma", 44);
        put("Minus", 45);
        put("Period", 46);
        put("Slash", 47);

        // Digits (top row)
        put("Digit0", 48);
        put("Digit1", 49);
        put("Digit2", 50);
        put("Digit3", 51);
        put("Digit4", 52);
        put("Digit5", 53);
        put("Digit6", 54);
        put("Digit7", 55);
        put("Digit8", 56);
        put("Digit9", 57);

        put("Semicolon", 59);
        put("Equal", 61);

        // Letters (GLFW uses uppercase ASCII values 65..90)
        put("KeyA", 65);
        put("KeyB", 66);
        put("KeyC", 67);
        put("KeyD", 68);
        put("KeyE", 69);
        put("KeyF", 70);
        put("KeyG", 71);
        put("KeyH", 72);
        put("KeyI", 73);
        put("KeyJ", 74);
        put("KeyK", 75);
        put("KeyL", 76);
        put("KeyM", 77);
        put("KeyN", 78);
        put("KeyO", 79);
        put("KeyP", 80);
        put("KeyQ", 81);
        put("KeyR", 82);
        put("KeyS", 83);
        put("KeyT", 84);
        put("KeyU", 85);
        put("KeyV", 86);
        put("KeyW", 87);
        put("KeyX", 88);
        put("KeyY", 89);
        put("KeyZ", 90);

        put("BracketLeft", 91);
        put("Backslash", 92);
        put("BracketRight", 93);
        put("Backquote", 96);      // GLFW_KEY_GRAVE_ACCENT

        // Function / control keys
        put("Escape", 256);
        put("Enter", 257);
        put("Tab", 258);
        put("Backspace", 259);
        put("Insert", 260);
        put("Delete", 261);
        put("ArrowRight", 262);
        put("ArrowLeft", 263);
        put("ArrowDown", 264);
        put("ArrowUp", 265);
        put("PageUp", 266);
        put("PageDown", 267);
        put("Home", 268);
        put("End", 269);
        put("CapsLock", 280);
        put("ScrollLock", 281);
        put("NumLock", 282);
        put("PrintScreen", 283);
        put("Pause", 284);

        // Function keys F1..F25
        put("F1", 290);
        put("F2", 291);
        put("F3", 292);
        put("F4", 293);
        put("F5", 294);
        put("F6", 295);
        put("F7", 296);
        put("F8", 297);
        put("F9", 298);
        put("F10", 299);
        put("F11", 300);
        put("F12", 301);
        put("F13", 302);
        put("F14", 303);
        put("F15", 304);
        put("F16", 305);
        put("F17", 306);
        put("F18", 307);
        put("F19", 308);
        put("F20", 309);
        put("F21", 310);
        put("F22", 311);
        put("F23", 312);
        put("F24", 313);

        // Keypad
        put("Numpad0", 320);
        put("Numpad1", 321);
        put("Numpad2", 322);
        put("Numpad3", 323);
        put("Numpad4", 324);
        put("Numpad5", 325);
        put("Numpad6", 326);
        put("Numpad7", 327);
        put("Numpad8", 328);
        put("Numpad9", 329);
        put("NumpadDecimal", 330);
        put("NumpadDivide", 331);
        put("NumpadMultiply", 332);
        put("NumpadSubtract", 333);
        put("NumpadAdd", 334);
        put("NumpadEnter", 335);
        put("NumpadEqual", 336);

        // Modifiers
        put("ShiftLeft", 340);
        put("ControlLeft", 341);
        put("AltLeft", 342);
        put("MetaLeft", 343);      // GLFW_KEY_LEFT_SUPER
        put("ShiftRight", 344);
        put("ControlRight", 345);
        put("AltRight", 346);
        put("MetaRight", 347);     // GLFW_KEY_RIGHT_SUPER
        put("ContextMenu", 348);   // GLFW_KEY_MENU
    }
}
