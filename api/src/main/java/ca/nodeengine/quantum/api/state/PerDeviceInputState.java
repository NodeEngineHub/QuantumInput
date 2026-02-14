package ca.nodeengine.quantum.api.state;

import ca.nodeengine.quantum.api.InputDevice;

/**
 * An input state where you must provide the device you would like to get the input from.
 *
 * @author FX
 */
public interface PerDeviceInputState extends InputState {

    /**
     * Checks if a key is being pressed.
     *
     * @param device The device to check the key of
     * @param code   The key code
     * @return {@code true} if the key is being pressed, otherwise {@code false}
     */
    boolean isKeyPressed(InputDevice device, int code);

    /**
     * Checks if a key is being held.
     *
     * @param device The device to check the key of
     * @param code   The key code
     * @return {@code true} if the key is being held, otherwise {@code false}
     */
    boolean isKeyHeld(InputDevice device, int code);

    /**
     * Checks if a key is being released.
     *
     * @param device The device to check the key of
     * @param code   The key code
     * @return {@code true} if the key is being released, otherwise {@code false}
     */
    boolean isKeyReleased(InputDevice device, int code);

    /**
     * Checks if a mouse button is being pressed.
     *
     * @param device The device to check the button of
     * @param code   The mouse button code
     * @return {@code true} if the mouse button is being pressed, otherwise {@code false}
     */
    boolean isButtonPressed(InputDevice device, int code);

    /**
     * Checks if a mouse button is being held.
     *
     * @param device The device to check the button of
     * @param code   The mouse button code
     * @return {@code true} if the mouse button is being held, otherwise {@code false}
     */
    boolean isButtonHeld(InputDevice device, int code);

    /**
     * Checks if a mouse button is being released.
     *
     * @param device The device to check the button of
     * @param code   The mouse button code
     * @return {@code true} if the mouse button is being released, otherwise {@code false}
     */
    boolean isButtonReleased(InputDevice device, int code);

    /**
     * Gets an axis's values.
     *
     * @param device   The device to check the axis of
     * @param axisCode The axis's code
     * @return The current value of the axis, or {@code 0} if it's not set.
     */
    float getAxis(InputDevice device, int axisCode);

    /**
     * Gets a mouses position.<br>
     * Don't modify the returned array, the same array may be reused.
     *
     * @param device The device to get the mouse position from.
     * @return An array representing the mouse position as [x, y]
     */
    float[] getMouse(InputDevice device);

    /**
     * Gets the scroll amount.<br>
     * Don't modify the returned array, the same array may be reused.
     *
     * @param device The device to get the scroll velocity from.
     * @return An array representing the scroll velocity as [xVelocity, yVelocity]
     */
    float[] getScroll(InputDevice device);
}
