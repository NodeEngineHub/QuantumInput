package ca.nodeengine.quantum;

/**
 * Canonical keyboard key codes used throughout QuantumInput.
 * <p>
 * This is QuantumInput's own key table. Every keyboard platform normalizes its native key
 * representation (GLFW keycodes, DOM {@code KeyboardEvent.code} strings, AWT {@code VK_*}, JavaFX
 * {@code KeyCode}, SDL keycodes, ...) to these values, so an application sees identical key codes across
 * platforms.
 * <p>
 * The values for the "standard" keys deliberately match the <b>GLFW</b> keycodes (like {@link MouseButtons}
 * is based on GLFW button numbering): the GLFW platform therefore passes its codes through unchanged, and
 * the other platforms map into the same space.
 * <p>
 * A platform reports {@link #UNKNOWN} for a key it cannot map to any entry here (yet).
 *
 * @author FX
 * @see Settings#STANDARDIZE_KEYS
 */
public final class Keys {

    /** A key with no canonical mapping. */
    public static final int UNKNOWN = -1;

    //region Individual (Not in GLFW)

    public static final int INVERTED_EXCLAIM = 0; // ¡
    public static final int EXCLAIM = 1;          // !
    public static final int DOUBLE_QUOTE = 2;     // "
    public static final int HASH = 3;             // #
    public static final int DOLLAR = 4;           // $
    public static final int PERCENT = 5;          // %
    public static final int AMPERSAND = 6;        // &
    public static final int OPEN_PAREN = 7;       // (
    public static final int CLOSE_PAREN = 8;      // )
    public static final int ASTERISK = 9;         // *
    public static final int PLUS = 10;            // +
    public static final int COLON = 11;           // :
    public static final int LESS = 12;            // <
    public static final int GREATER = 13;         // >
    public static final int QUESTION = 14;        // ?
    public static final int AT = 15;              // @
    public static final int CARET = 16;           // ^
    public static final int UNDERSCORE = 17;      // _
    public static final int PIPE = 18;            // |
    public static final int TILDE = 19;           // ~

    //endregion

    //region Printable

    public static final int SPACE = 32;
    public static final int QUOTE = 39;           // '
    public static final int COMMA = 44;           // ,
    public static final int MINUS = 45;           // -
    public static final int PERIOD = 46;          // .
    public static final int SLASH = 47;           // /

    public static final int DIGIT_0 = 48;
    public static final int DIGIT_1 = 49;
    public static final int DIGIT_2 = 50;
    public static final int DIGIT_3 = 51;
    public static final int DIGIT_4 = 52;
    public static final int DIGIT_5 = 53;
    public static final int DIGIT_6 = 54;
    public static final int DIGIT_7 = 55;
    public static final int DIGIT_8 = 56;
    public static final int DIGIT_9 = 57;

    public static final int SEMICOLON = 59;       // ;
    public static final int EQUAL = 61;           // =

    public static final int A = 65;
    public static final int B = 66;
    public static final int C = 67;
    public static final int D = 68;
    public static final int E = 69;
    public static final int F = 70;
    public static final int G = 71;
    public static final int H = 72;
    public static final int I = 73;
    public static final int J = 74;
    public static final int K = 75;
    public static final int L = 76;
    public static final int M = 77;
    public static final int N = 78;
    public static final int O = 79;
    public static final int P = 80;
    public static final int Q = 81;
    public static final int R = 82;
    public static final int S = 83;
    public static final int T = 84;
    public static final int U = 85;
    public static final int V = 86;
    public static final int W = 87;
    public static final int X = 88;
    public static final int Y = 89;
    public static final int Z = 90;

    public static final int OPEN_BRACKET = 91;    // [
    public static final int BACKSLASH = 92;       // \
    public static final int CLOSE_BRACKET = 93;   // ]
    public static final int GRAVE_ACCENT = 96;    // `
    //endregion

    //region Function / control
    public static final int ESCAPE = 256;
    public static final int ENTER = 257;
    public static final int TAB = 258;
    public static final int BACKSPACE = 259;
    public static final int INSERT = 260;
    public static final int DELETE = 261;
    public static final int RIGHT = 262;
    public static final int LEFT = 263;
    public static final int DOWN = 264;
    public static final int UP = 265;
    public static final int PAGE_UP = 266;
    public static final int PAGE_DOWN = 267;
    public static final int HOME = 268;
    public static final int END = 269;
    public static final int CAPS_LOCK = 280;
    public static final int SCROLL_LOCK = 281;
    public static final int NUM_LOCK = 282;
    public static final int PRINT_SCREEN = 283;
    public static final int PAUSE = 284;

    public static final int F1 = 290;
    public static final int F2 = 291;
    public static final int F3 = 292;
    public static final int F4 = 293;
    public static final int F5 = 294;
    public static final int F6 = 295;
    public static final int F7 = 296;
    public static final int F8 = 297;
    public static final int F9 = 298;
    public static final int F10 = 299;
    public static final int F11 = 300;
    public static final int F12 = 301;
    public static final int F13 = 302;
    public static final int F14 = 303;
    public static final int F15 = 304;
    public static final int F16 = 305;
    public static final int F17 = 306;
    public static final int F18 = 307;
    public static final int F19 = 308;
    public static final int F20 = 309;
    public static final int F21 = 310;
    public static final int F22 = 311;
    public static final int F23 = 312;
    public static final int F24 = 313;
    public static final int F25 = 314;
    //endregion

    //region Keypad
    public static final int NUMPAD_0 = 320;
    public static final int NUMPAD_1 = 321;
    public static final int NUMPAD_2 = 322;
    public static final int NUMPAD_3 = 323;
    public static final int NUMPAD_4 = 324;
    public static final int NUMPAD_5 = 325;
    public static final int NUMPAD_6 = 326;
    public static final int NUMPAD_7 = 327;
    public static final int NUMPAD_8 = 328;
    public static final int NUMPAD_9 = 329;
    public static final int NUMPAD_DECIMAL = 330;
    public static final int NUMPAD_DIVIDE = 331;
    public static final int NUMPAD_MULTIPLY = 332;
    public static final int NUMPAD_SUBTRACT = 333;
    public static final int NUMPAD_ADD = 334;
    public static final int NUMPAD_ENTER = 335;
    public static final int NUMPAD_EQUAL = 336;
    // Not defined in GLFW
    public static final int NUMPAD_UP = 315;
    public static final int NUMPAD_DOWN = 316;
    public static final int NUMPAD_LEFT = 317;
    public static final int NUMPAD_RIGHT = 318;
    //endregion

    //region Modifiers
    public static final int LEFT_SHIFT = 340;
    public static final int LEFT_CONTROL = 341;
    public static final int LEFT_ALT = 342;
    public static final int LEFT_SUPER = 343;     // Windows / Command / Meta
    public static final int RIGHT_SHIFT = 344;
    public static final int RIGHT_CONTROL = 345;
    public static final int RIGHT_ALT = 346;
    public static final int RIGHT_SUPER = 347;
    public static final int MENU = 348;           // context-menu key
    //endregion

    //region Extended (QuantumInput-specific; keys GLFW does not define)

    /**
     * First value of the QuantumInput-specific extended range. Everything at or above this is a key GLFW
     * has no code for; add new cross-platform keys here (values are chosen not to collide with GLFW's).
     */
    public static final int EXTENDED_START = 400;

    // International / IME
    public static final int INTL_BACKSLASH = 400; // extra key next to left shift on ISO layouts
    public static final int INTL_RO = 401;        // JIS \ / _
    public static final int INTL_YEN = 402;       // JIS ¥
    public static final int LANG_1 = 403;         // Hangul / Kana toggle
    public static final int LANG_2 = 404;         // Hanja / Eisu toggle
    public static final int CONVERT = 405;        // 変換
    public static final int NON_CONVERT = 406;    // 無変換
    public static final int KANA = 407;

    // Media
    public static final int MEDIA_PLAY_PAUSE = 500;
    public static final int MEDIA_STOP = 501;
    public static final int MEDIA_NEXT = 502;
    public static final int MEDIA_PREVIOUS = 503;
    public static final int VOLUME_UP = 504;
    public static final int VOLUME_DOWN = 505;
    public static final int VOLUME_MUTE = 506;

    // Browser / launcher
    public static final int BROWSER_BACK = 600;
    public static final int BROWSER_FORWARD = 601;
    public static final int BROWSER_REFRESH = 602;
    public static final int BROWSER_HOME = 603;
    public static final int BROWSER_SEARCH = 604;

    // System
    public static final int POWER = 700;
    public static final int SLEEP = 701;
    public static final int WAKE = 702;
    public static final int HIBERNATE = 703;
    public static final int STANDBY = 704;

    // Application / editing
    public static final int HELP = 800;
    public static final int SELECT = 801;
    public static final int AGAIN = 802;
    public static final int UNDO = 803;
    public static final int CUT = 804;
    public static final int COPY = 805;
    public static final int PASTE = 806;
    public static final int FIND = 807;

    // Actions
    public static final int CANCEL = 900;
    public static final int CLEAR = 901;
    public static final int EXECUTE = 902;
    public static final int RETURN = 903;
    //endregion

    private Keys() {}
}
