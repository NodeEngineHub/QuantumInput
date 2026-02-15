package ca.nodeengine.quantum.api.state;

/**
 * An input state representing all devices combined.
 * <p>
 * This is used when the platform doesn't support per-device access or when
 * the application only cares about the aggregate state of all input devices.
 *
 * @author FX
 */
public interface GlobalInputState extends InputState {

    /**
     * Checks if a key was pressed during the last update.
     *
     * @param code The key code.
     * @return {@code true} if the key was pressed, otherwise {@code false}.
     */
    boolean isKeyPressed(int code);

    /**
     * Checks if a key is currently being held down.
     *
     * @param code The key code.
     * @return {@code true} if the key is held, otherwise {@code false}.
     */
    boolean isKeyHeld(int code);

    /**
     * Checks if a key was released during the last update.
     *
     * @param code The key code.
     * @return {@code true} if the key was released, otherwise {@code false}.
     */
    boolean isKeyReleased(int code);

    /**
     * Checks if a mouse button was pressed during the last update.
     *
     * @param code The mouse button code.
     * @return {@code true} if the mouse button was pressed, otherwise {@code false}.
     */
    boolean isButtonPressed(int code);

    /**
     * Checks if a mouse button is currently being held down.
     *
     * @param code The mouse button code.
     * @return {@code true} if the mouse button is held, otherwise {@code false}.
     */
    boolean isButtonHeld(int code);

    /**
     * Checks if a mouse button was released during the last update.
     *
     * @param code The mouse button code.
     * @return {@code true} if the mouse button was released, otherwise {@code false}.
     */
    boolean isButtonReleased(int code);

    /**
     * Gets the value of an analog axis.
     *
     * @param axisCode The axis code.
     * @return The current value of the axis, or {@code 0} if it's not set.
     */
    float getAxis(int axisCode);

    /**
     * Gets the mouse position.
     * <p>
     * NOTE: Do not modify the returned array; the same array instance may be reused.
     *
     * @return An array representing the mouse position as [x, y].
     */
    float[] getMouse();

    /**
     * Gets the mouse scroll amount.
     * <p>
     * NOTE: Do not modify the returned array; the same array instance may be reused.
     *
     * @return An array representing the scroll velocity as [xVelocity, yVelocity].
     */
    float[] getScroll();
}
