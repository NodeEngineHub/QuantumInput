package ca.nodeengine.quantum;

import java.util.Locale;

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


    //region Helper Methods

    /**
     * Allows you to get the key code from a key name.
     *
     * @param name The name of the key
     * @return The key code
     */
    public static int fromName(String name) {
        return switch (name.toUpperCase(Locale.ROOT)) {
            case "INVERTED_EXCLAIM" -> INVERTED_EXCLAIM;
            case "EXCLAIM" -> EXCLAIM;
            case "DOUBLE_QUOTE" -> DOUBLE_QUOTE;
            case "HASH" -> HASH;
            case "DOLLAR" -> DOLLAR;
            case "PERCENT" -> PERCENT;
            case "AMPERSAND" -> AMPERSAND;
            case "OPEN_PAREN" -> OPEN_PAREN;
            case "CLOSE_PAREN" -> CLOSE_PAREN;
            case "ASTERISK" -> ASTERISK;
            case "PLUS" -> PLUS;
            case "COLON" -> COLON;
            case "LESS" -> LESS;
            case "GREATER" -> GREATER;
            case "QUESTION" -> QUESTION;
            case "AT" -> AT;
            case "CARET" -> CARET;
            case "UNDERSCORE" -> UNDERSCORE;
            case "PIPE" -> PIPE;
            case "TILDE" -> TILDE;
            case "SPACE" -> SPACE;
            case "QUOTE" -> QUOTE;
            case "COMMA" -> COMMA;
            case "MINUS" -> MINUS;
            case "PERIOD" -> PERIOD;
            case "SLASH" -> SLASH;
            case "DIGIT_0" -> DIGIT_0;
            case "DIGIT_1" -> DIGIT_1;
            case "DIGIT_2" -> DIGIT_2;
            case "DIGIT_3" -> DIGIT_3;
            case "DIGIT_4" -> DIGIT_4;
            case "DIGIT_5" -> DIGIT_5;
            case "DIGIT_6" -> DIGIT_6;
            case "DIGIT_7" -> DIGIT_7;
            case "DIGIT_8" -> DIGIT_8;
            case "DIGIT_9" -> DIGIT_9;
            case "SEMICOLON" -> SEMICOLON;
            case "EQUAL" -> EQUAL;
            case "A" -> A;
            case "B" -> B;
            case "C" -> C;
            case "D" -> D;
            case "E" -> E;
            case "F" -> F;
            case "G" -> G;
            case "H" -> H;
            case "I" -> I;
            case "J" -> J;
            case "K" -> K;
            case "L" -> L;
            case "M" -> M;
            case "N" -> N;
            case "O" -> O;
            case "P" -> P;
            case "Q" -> Q;
            case "R" -> R;
            case "S" -> S;
            case "T" -> T;
            case "U" -> U;
            case "V" -> V;
            case "W" -> W;
            case "X" -> X;
            case "Y" -> Y;
            case "Z" -> Z;
            case "OPEN_BRACKET" -> OPEN_BRACKET;
            case "BACKSLASH" -> BACKSLASH;
            case "CLOSE_BRACKET" -> CLOSE_BRACKET;
            case "GRAVE_ACCENT" -> GRAVE_ACCENT;
            case "ESCAPE" -> ESCAPE;
            case "ENTER" -> ENTER;
            case "TAB" -> TAB;
            case "BACKSPACE" -> BACKSPACE;
            case "INSERT" -> INSERT;
            case "DELETE" -> DELETE;
            case "RIGHT" -> RIGHT;
            case "LEFT" -> LEFT;
            case "DOWN" -> DOWN;
            case "UP" -> UP;
            case "PAGE_UP" -> PAGE_UP;
            case "PAGE_DOWN" -> PAGE_DOWN;
            case "HOME" -> HOME;
            case "END" -> END;
            case "CAPS_LOCK" -> CAPS_LOCK;
            case "SCROLL_LOCK" -> SCROLL_LOCK;
            case "NUM_LOCK" -> NUM_LOCK;
            case "PRINT_SCREEN" -> PRINT_SCREEN;
            case "PAUSE" -> PAUSE;
            case "F1" -> F1;
            case "F2" -> F2;
            case "F3" -> F3;
            case "F4" -> F4;
            case "F5" -> F5;
            case "F6" -> F6;
            case "F7" -> F7;
            case "F8" -> F8;
            case "F9" -> F9;
            case "F10" -> F10;
            case "F11" -> F11;
            case "F12" -> F12;
            case "F13" -> F13;
            case "F14" -> F14;
            case "F15" -> F15;
            case "F16" -> F16;
            case "F17" -> F17;
            case "F18" -> F18;
            case "F19" -> F19;
            case "F20" -> F20;
            case "F21" -> F21;
            case "F22" -> F22;
            case "F23" -> F23;
            case "F24" -> F24;
            case "F25" -> F25;
            case "NUMPAD_0" -> NUMPAD_0;
            case "NUMPAD_1" -> NUMPAD_1;
            case "NUMPAD_2" -> NUMPAD_2;
            case "NUMPAD_3" -> NUMPAD_3;
            case "NUMPAD_4" -> NUMPAD_4;
            case "NUMPAD_5" -> NUMPAD_5;
            case "NUMPAD_6" -> NUMPAD_6;
            case "NUMPAD_7" -> NUMPAD_7;
            case "NUMPAD_8" -> NUMPAD_8;
            case "NUMPAD_9" -> NUMPAD_9;
            case "NUMPAD_DECIMAL" -> NUMPAD_DECIMAL;
            case "NUMPAD_DIVIDE" -> NUMPAD_DIVIDE;
            case "NUMPAD_MULTIPLY" -> NUMPAD_MULTIPLY;
            case "NUMPAD_SUBTRACT" -> NUMPAD_SUBTRACT;
            case "NUMPAD_ADD" -> NUMPAD_ADD;
            case "NUMPAD_ENTER" -> NUMPAD_ENTER;
            case "NUMPAD_EQUAL" -> NUMPAD_EQUAL;
            case "NUMPAD_UP" -> NUMPAD_UP;
            case "NUMPAD_DOWN" -> NUMPAD_DOWN;
            case "NUMPAD_LEFT" -> NUMPAD_LEFT;
            case "NUMPAD_RIGHT" -> NUMPAD_RIGHT;
            case "LEFT_SHIFT" -> LEFT_SHIFT;
            case "LEFT_CONTROL" -> LEFT_CONTROL;
            case "LEFT_ALT" -> LEFT_ALT;
            case "LEFT_SUPER" -> LEFT_SUPER;
            case "RIGHT_SHIFT" -> RIGHT_SHIFT;
            case "RIGHT_CONTROL" -> RIGHT_CONTROL;
            case "RIGHT_ALT" -> RIGHT_ALT;
            case "RIGHT_SUPER" -> RIGHT_SUPER;
            case "MENU" -> MENU;
            case "EXTENDED_START" -> EXTENDED_START;
            case "INTL_BACKSLASH" -> INTL_BACKSLASH;
            case "INTL_RO" -> INTL_RO;
            case "INTL_YEN" -> INTL_YEN;
            case "LANG_1" -> LANG_1;
            case "LANG_2" -> LANG_2;
            case "CONVERT" -> CONVERT;
            case "NON_CONVERT" -> NON_CONVERT;
            case "KANA" -> KANA;
            case "MEDIA_PLAY_PAUSE" -> MEDIA_PLAY_PAUSE;
            case "MEDIA_STOP" -> MEDIA_STOP;
            case "MEDIA_NEXT" -> MEDIA_NEXT;
            case "MEDIA_PREVIOUS" -> MEDIA_PREVIOUS;
            case "VOLUME_UP" -> VOLUME_UP;
            case "VOLUME_DOWN" -> VOLUME_DOWN;
            case "VOLUME_MUTE" -> VOLUME_MUTE;
            case "BROWSER_BACK" -> BROWSER_BACK;
            case "BROWSER_FORWARD" -> BROWSER_FORWARD;
            case "BROWSER_REFRESH" -> BROWSER_REFRESH;
            case "BROWSER_HOME" -> BROWSER_HOME;
            case "BROWSER_SEARCH" -> BROWSER_SEARCH;
            case "POWER" -> POWER;
            case "SLEEP" -> SLEEP;
            case "WAKE" -> WAKE;
            case "HIBERNATE" -> HIBERNATE;
            case "STANDBY" -> STANDBY;
            case "HELP" -> HELP;
            case "SELECT" -> SELECT;
            case "AGAIN" -> AGAIN;
            case "UNDO" -> UNDO;
            case "CUT" -> CUT;
            case "COPY" -> COPY;
            case "PASTE" -> PASTE;
            case "FIND" -> FIND;
            case "CANCEL" -> CANCEL;
            case "CLEAR" -> CLEAR;
            case "EXECUTE" -> EXECUTE;
            case "RETURN" -> RETURN;
            default -> -1;
        };
    }

    /**
     * Allows you to get the key code from a character.<br>
     * Many keys don't have a character representation, and therefore cannot be retrieved using this method.
     *
     * @param c The character to get the key code from
     * @return The key code
     */
    public static int fromChar(char c) {
        return switch (c) {
            case '¡' -> INVERTED_EXCLAIM;
            case '!' -> EXCLAIM;
            case '"' -> DOUBLE_QUOTE;
            case '#' -> HASH;
            case '$' -> DOLLAR;
            case '%' -> PERCENT;
            case '&' -> AMPERSAND;
            case '(' -> OPEN_PAREN;
            case ')' -> CLOSE_PAREN;
            case '*' -> ASTERISK;
            case '+' -> PLUS;
            case ':' -> COLON;
            case '<' -> LESS;
            case '>' -> GREATER;
            case '?' -> QUESTION;
            case '@' -> AT;
            case '^' -> CARET;
            case '_' -> UNDERSCORE;
            case '|' -> PIPE;
            case '~' -> TILDE;
            case ' ' -> SPACE;
            case '\'' -> QUOTE;
            case ',' -> COMMA;
            case '-' -> MINUS;
            case '.' -> PERIOD;
            case '/' -> SLASH;
            case '0' -> DIGIT_0;
            case '1' -> DIGIT_1;
            case '2' -> DIGIT_2;
            case '3' -> DIGIT_3;
            case '4' -> DIGIT_4;
            case '5' -> DIGIT_5;
            case '6' -> DIGIT_6;
            case '7' -> DIGIT_7;
            case '8' -> DIGIT_8;
            case '9' -> DIGIT_9;
            case ';' -> SEMICOLON;
            case '=' -> EQUAL;
            case 'A', 'a' -> A;
            case 'B', 'b' -> B;
            case 'C', 'c' -> C;
            case 'D', 'd' -> D;
            case 'E', 'e' -> E;
            case 'F', 'f' -> F;
            case 'G', 'g' -> G;
            case 'H', 'h' -> H;
            case 'I', 'i' -> I;
            case 'J', 'j' -> J;
            case 'K', 'k' -> K;
            case 'L', 'l' -> L;
            case 'M', 'm' -> M;
            case 'N', 'n' -> N;
            case 'O', 'o' -> O;
            case 'P', 'p' -> P;
            case 'Q', 'q' -> Q;
            case 'R', 'r' -> R;
            case 'S', 's' -> S;
            case 'T', 't' -> T;
            case 'U', 'u' -> U;
            case 'V', 'v' -> V;
            case 'W', 'w' -> W;
            case 'X', 'x' -> X;
            case 'Y', 'y' -> Y;
            case 'Z', 'z' -> Z;
            case '[' -> OPEN_BRACKET;
            case '\\' -> BACKSLASH;
            case ']' -> CLOSE_BRACKET;
            case '`' -> GRAVE_ACCENT;
            case '\u001B' -> ESCAPE;
            case '\u0008' -> BACKSPACE;
            case '\u007F' -> DELETE;
            case '\u0003', '\u0004' -> END;
            case '¥' -> INTL_YEN;
            case '\u0018' -> CANCEL;
            default -> -1;
        };
    }
    //endregion
}
