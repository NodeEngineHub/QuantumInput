package ca.nodeengine.quantum.state;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.InputProcessor;

/**
 * Unified Global & Per Device input state mutations.<br>
 * For use internally within the {@link InputProcessor} system.
 *
 * @author FX
 */
public interface MutableInputState {

    /**
     * Sets the mouse position
     *
     * @param device The input device
     * @param x      The x position
     * @param y      The y position
     */
    void setMousePos(InputDevice device, float x, float y);

    /**
     * Sets the mouse scroll
     *
     * @param device  The input device
     * @param xOffset The x offset
     * @param yOffset The y offset
     */
    void setScroll(InputDevice device, float xOffset, float yOffset);

    /**
     * Sets a key as pressed.<br>
     * The platform MUST always give a pressed and released for every key.
     *
     * @param device The input device
     * @param code   The key code
     * @param press  If this is a pressed event, otherwise it will be treated as release.
     */
    void setKey(InputDevice device, int code, boolean press);

    /**
     * Sets a mouse button as pressed.<br>
     * The platform MUST always give a pressed and released for every button.
     *
     * @param device The input device
     * @param code   The mouse button code
     * @param press  If this is a pressed event, otherwise it will be treated as release.
     */
    void setButton(InputDevice device, int code, boolean press);

    /**
     * Sets an axis's value
     *
     * @param device The input device
     * @param axis   The axis code
     * @param value  The value to set
     */
    void setAxis(InputDevice device, int axis, float value);
}
