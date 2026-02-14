package ca.nodeengine.quantum.api.state;

/**
 * An input state representing all devices.
 *
 * @author FX
 */
public interface GlobalInputState extends InputState {

    /**
     * Checks if a key is being pressed.
     *
     * @param code The key code
     * @return {@code true} if the key is being pressed, otherwise {@code false}
     */
    boolean isKeyPressed(int code);

    /**
     * Checks if a key is being held.
     *
     * @param code The key code
     * @return {@code true} if the key is being held, otherwise {@code false}
     */
    boolean isKeyHeld(int code);

    /**
     * Checks if a key is being released.
     *
     * @param code The key code
     * @return {@code true} if the key is being released, otherwise {@code false}
     */
    boolean isKeyReleased(int code);

    /**
     * Checks if a mouse button is being pressed.
     *
     * @param code The mouse button code
     * @return {@code true} if the mouse button is being pressed, otherwise {@code false}
     */
    boolean isButtonPressed(int code);

    /**
     * Checks if a mouse button is being held.
     *
     * @param code The mouse button code
     * @return {@code true} if the mouse button is being held, otherwise {@code false}
     */
    boolean isButtonHeld(int code);

    /**
     * Checks if a mouse button is being released.
     *
     * @param code The mouse button code
     * @return {@code true} if the mouse button is being released, otherwise {@code false}
     */
    boolean isButtonReleased(int code);

    /**
     * Gets an axis's values.
     *
     * @param axisCode The axis's code
     * @return The current value of the axis, or {@code 0} if it's not set.
     */
    float getAxis(int axisCode);

    /**
     * Gets a mouses position.<br>
     * Don't modify the returned array, the same array may be reused.
     *
     * @return An array representing the mouse position as [x, y]
     */
    float[] getMouse();

    /**
     * Gets the scroll amount.<br>
     * Don't modify the returned array, the same array may be reused.
     *
     * @return An array representing the scroll velocity as [xVelocity, yVelocity]
     */
    float[] getScroll();
}
