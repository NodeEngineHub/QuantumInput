package ca.nodeengine.quantum.state;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.InputProcessor;

/**
 * Unified interface for Global &amp; Per-Device input state mutations.
 * <p>
 * This interface is used internally within the {@link InputProcessor} system
 * to update the input state based on platform events.
 * </p>
 *
 * @author FX
 */
public interface MutableInputState {

    /**
     * Sets the mouse position for a specific device.
     *
     * @param device The input device.
     * @param x      The x position.
     * @param y      The y position.
     */
    void setMousePos(InputDevice device, float x, float y);

    /**
     * Sets the mouse scroll velocity for a specific device.
     *
     * @param device  The input device.
     * @param xOffset The x velocity.
     * @param yOffset The y velocity.
     */
    void setScroll(InputDevice device, float xOffset, float yOffset);

    /**
     * Sets a key's state for a specific device.
     * <p>
     * The platform MUST always provide both pressed and released events for every key.
     * </p>
     *
     * @param device The input device.
     * @param code   The key code.
     * @param press  {@code true} if pressed, {@code false} if released.
     */
    void setKey(InputDevice device, int code, boolean press);

    /**
     * Sets a mouse button's state for a specific device.
     * <p>
     * The platform MUST always provide both pressed and released events for every button.
     * </p>
     *
     * @param device The input device.
     * @param code   The mouse button code.
     * @param press  {@code true} if pressed, {@code false} if released.
     */
    void setButton(InputDevice device, int code, boolean press);

    /**
     * Sets an analog axis value for a specific device.
     *
     * @param device The input device.
     * @param axis   The axis code.
     * @param value  The value to set.
     */
    void setAxis(InputDevice device, int axis, float value);
}
