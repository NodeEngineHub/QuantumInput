package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.event.InputEvent;
import ca.nodeengine.quantum.state.InputState;

import java.util.Arrays;
import java.util.List;

/**
 * A binding that matches if any of its components match.
 * <p>
 * This acts as an OR between multiple bindings.
 *
 * @author FX
 */
public final class OrBinding implements ActionBinding {

    private final String action;
    private final List<ActionBinding> components;

    public OrBinding(String action, ActionBinding... components) {
        this.action = action;
        this.components = Arrays.asList(components);
    }

    @Override
    public String action() {
        return action;
    }

    @Override
    public boolean matches(InputState state) {
        for (ActionBinding component : components) {
            if (component.matches(state)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public float value(InputState state) {
        float max = 0F;
        for (ActionBinding component : components) {
            max = Math.max(max, component.value(state));
        }
        return max;
    }

    @Override
    public boolean isPressed(InputState state) {
        for (ActionBinding component : components) {
            if (component.isPressed(state)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isReleased(InputState state) {
        // If ORing, it's released if all were held and at least one is released? 
        // Or if it was active and now it's not?
        // Let's follow the simple logic: if any component is released.
        for (ActionBinding component : components) {
            if (component.isReleased(state)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isTriggeredBy(InputEvent event) {
        for (ActionBinding component : components) {
            if (component.isTriggeredBy(event)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public float value(InputEvent event) {
        float max = 0F;
        for (ActionBinding component : components) {
            if (component.isTriggeredBy(event)) {
                max = Math.max(max, component.value(event));
            }
        }
        return max;
    }
}
