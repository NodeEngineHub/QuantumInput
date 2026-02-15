package ca.nodeengine.quantum.api.state;

import ca.nodeengine.quantum.api.InputDevice;

/**
 * An input state where you must specify the device to retrieve input from.
 * <p>
 * This is used when the platform supports multiple input devices of the same type
 * and the application needs to distinguish between them.
 * </p>
 *
 * @author FX
 */
public interface PerDeviceInputState extends InputState {

    /**
     * Checks if a key was pressed on a specific device during the last update.
     *
     * @param device The device to check.
     * @param code   The key code.
     * @return {@code true} if the key was pressed, otherwise {@code false}.
     */
    boolean isKeyPressed(InputDevice device, int code);

    /**
     * Checks if a key is currently being held down on a specific device.
     *
     * @param device The device to check.
     * @param code   The key code.
     * @return {@code true} if the key is held, otherwise {@code false}.
     */
    boolean isKeyHeld(InputDevice device, int code);

    /**
     * Checks if a key was released on a specific device during the last update.
     *
     * @param device The device to check.
     * @param code   The key code.
     * @return {@code true} if the key was released, otherwise {@code false}.
     */
    boolean isKeyReleased(InputDevice device, int code);

    /**
     * Checks if a mouse button was pressed on a specific device during the last update.
     *
     * @param device The device to check.
     * @param code   The mouse button code.
     * @return {@code true} if the mouse button was pressed, otherwise {@code false}.
     */
    boolean isButtonPressed(InputDevice device, int code);

    /**
     * Checks if a mouse button is currently being held down on a specific device.
     *
     * @param device The device to check.
     * @param code   The mouse button code.
     * @return {@code true} if the mouse button is held, otherwise {@code false}.
     */
    boolean isButtonHeld(InputDevice device, int code);

    /**
     * Checks if a mouse button was released on a specific device during the last update.
     *
     * @param device The device to check.
     * @param code   The mouse button code.
     * @return {@code true} if the mouse button was released, otherwise {@code false}.
     */
    boolean isButtonReleased(InputDevice device, int code);

    /**
     * Gets the value of an analog axis on a specific device.
     *
     * @param device   The device to check.
     * @param axisCode The axis code.
     * @return The current value of the axis, or {@code 0} if it's not set.
     */
    float getAxis(InputDevice device, int axisCode);

    /**
     * Gets the mouse position for a specific device.
     * <p>
     * NOTE: Do not modify the returned array; the same array instance may be reused.
     * </p>
     *
     * @param device The device to get the mouse position from.
     * @return An array representing the mouse position as [x, y].
     */
    float[] getMouse(InputDevice device);

    /**
     * Gets the mouse scroll amount for a specific device.
     * <p>
     * NOTE: Do not modify the returned array; the same array instance may be reused.
     * </p>
     *
     * @param device The device to get the scroll velocity from.
     * @return An array representing the scroll velocity as [xVelocity, yVelocity].
     */
    float[] getScroll(InputDevice device);
}
