package ca.nodeengine.quantum;

import java.util.Locale;

/**
 * Canonical mouse-button codes used throughout QuantumInput.
 * <p>
 * This enums is a table of mouse buttons, based heavily on the GLFW button numbering,
 * with some slight changes for multi-platform support.
 * We normalized all platforms to use these numbers. This keeps applications seeing identical
 * button codes across different platforms, such as GLFW and SDL.
 *
 * @author FX
 * @see Settings#STANDARDIZE_MOUSE_BUTTONS
 */
public final class MouseButtons {

    /** The left / primary mouse button ({@code GLFW_MOUSE_BUTTON_LEFT}). */
    public static final int LEFT = 0;

    /** The right / secondary mouse button ({@code GLFW_MOUSE_BUTTON_RIGHT}). */
    public static final int RIGHT = 1;

    /** The middle / auxiliary mouse button ({@code GLFW_MOUSE_BUTTON_MIDDLE}). */
    public static final int MIDDLE = 2;

    /** Extra Button 1 */
    public static final int BUTTON_1 = 3;

    /** Extra Button 2 */
    public static final int BUTTON_2 = 4;

    // 5+ for extra buttons

    /** If an event was fired without a button being pressed */
    public static final int NO_BUTTON = -1;


    private MouseButtons() {}


    //region Helper Methods

    /**
     * Allows you to get the mouse button from a button name.
     *
     * @param name The name of the button
     * @return The button code
     */
    public static int fromName(String name) {
        name = name.toUpperCase(Locale.ROOT);
        int code = switch (name) {
            case "LEFT" -> LEFT;
            case "RIGHT" -> RIGHT;
            case "MIDDLE" -> MIDDLE;
            default -> -1;
        };
        if (code != -1) {
            return code;
        }
        if (name.startsWith("BUTTON")) {
            return Integer.parseInt(2 + name.substring(7));
        }
        return -1;
    }
    //endregion
}