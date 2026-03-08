package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.InputDevice;
import ca.nodeengine.quantum.InputType;
import ca.nodeengine.quantum.event.InputEvent;
import ca.nodeengine.quantum.state.InputState;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

/**
 * Maps a high-level intent (action) to a specific hardware input.
 * <p>
 * This allows the application to query for "Jump" or "Fire" without
 * caring which specific key or button is bound to it.
 *
 * @author FX
 */
public interface ActionBinding {

    /**
     * Gets the name of the action this binding is for.
     *
     * @return The action name.
     */
    String action();

    /**
     * Checks if this binding is currently active (e.g., button is pressed).
     *
     * @param state The current input state to check against.
     * @return {@code true} if the binding matches, otherwise {@code false}.
     */
    boolean matches(InputState state);

    /**
     * Gets the current value of this binding.
     * <p>
     * For digital bindings, this will be {@code 0.0} or {@code 1.0}.
     * For analog bindings, this will be the axis value.
     *
     * @param state The current input state to check against.
     * @return The binding's value.
     */
    float value(InputState state);

    /**
     * Checks if this binding was just activated (e.g., button just pressed) in the current frame.
     *
     * @param state The current input state to check against.
     * @return {@code true} if just activated, otherwise {@code false}.
     */
    default boolean isPressed(InputState state) {
        return false;
    }

    /**
     * Checks if this binding was just deactivated (e.g., button just released) in the current frame.
     *
     * @param state The current input state to check against.
     * @return {@code true} if just deactivated, otherwise {@code false}.
     */
    default boolean isReleased(InputState state) {
        return false;
    }

    /**
     * Checks if this binding is triggered by the given input event.
     *
     * @param event The input event.
     * @return {@code true} if triggered, otherwise {@code false}.
     */
    boolean isTriggeredBy(InputEvent event);

    /**
     * Gets the value of this binding from the given input event.
     *
     * @param event The input event.
     * @return The binding's value from the event.
     */
    float value(InputEvent event);

    /**
     * Gets the primary input code that triggers this binding, or {@code -1} if it depends on multiple
     * or no specific code.
     * <p>
     * This is used for optimization of event dispatching.
     * </p>
     *
     * @return The trigger code.
     */
    default int triggerCode() {
        return -1;
    }

    /**
     * Gets the type of input this binding is for.
     *
     * @return The input type, or {@code null} if it's a composite or custom binding.
     */
    default @Nullable InputType type() {
        return null;
    }

    /**
     * Gets the device this binding is restricted to.
     *
     * @return The input device, or {@code null} if it's for all devices.
     */
    default @Nullable InputDevice device() {
        return null;
    }

    /**
     * Gets additional properties for this binding (e.g., deadzone, scale).
     *
     * @return A map of properties.
     */
    default Map<String, Object> properties() {
        return Collections.emptyMap();
    }
}
