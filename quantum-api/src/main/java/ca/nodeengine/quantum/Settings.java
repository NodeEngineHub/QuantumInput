package ca.nodeengine.quantum;

/**
 * Global settings for QuantumInput
 *
 * @author FX
 */
public final class Settings {

    /**
     * When enabled, all platforms will use the same mouse buttons.
     * Specifically those found in {@link MouseButtons}.
     */
    public static final boolean STANDARDIZE_MOUSE_BUTTONS = check("quantum.standardizeMouseButtons", true);


    private static boolean check(String key, boolean def) {
        return Boolean.parseBoolean(System.getProperty(key, String.valueOf(def)));
    }
}
