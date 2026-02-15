package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.action.ActionBinding;
import ca.nodeengine.quantum.api.event.InputEvent;
import ca.nodeengine.quantum.api.state.InputState;

import java.util.Arrays;
import java.util.List;

/**
 * A binding that requires multiple components to match.
 * <p>
 * This is typically used for key combinations like Ctrl+S.
 *
 * @author FX
 */
public final class CompositeBinding implements ActionBinding {

    private final String action;
    private final List<ActionBinding> components;

    public CompositeBinding(String action, ActionBinding... components) {
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
            if (!component.matches(state)) {
                return false;
            }
        }
        return !components.isEmpty();
    }

    @Override
    public float value(InputState state) {
        if (!matches(state)) {
            return 0F;
        }
        float min = 1F;
        for (ActionBinding component : components) {
            min = Math.min(min, component.value(state));
        }
        return min;
    }

    @Override
    public boolean isPressed(InputState state) {
        if (!matches(state)) {
            return false;
        }
        // It's pressed if it matches now, and at least one component was JUST pressed,
        // while others were already held or also just pressed.
        // Actually, a simpler way: matches now and didn't match before.
        // But we don't have an easy way to check "didn't match before" without previous state.
        
        // However, we can check if at least one component isPressed().
        boolean anyPressed = false;
        for (ActionBinding component : components) {
            if (component.isPressed(state)) {
                anyPressed = true;
                break;
            }
        }
        return anyPressed;
    }

    @Override
    public boolean isReleased(InputState state) {
        // It's released if it doesn't match now, but at least one component was JUST released,
        // while others were still held or just released.
        // Simplified: at least one component isReleased() and the others AREN'T held? 
        // No, that's not quite right.
        
        // If I release Ctrl in "Ctrl+S", the action is released.
        for (ActionBinding component : components) {
            if (component.isReleased(state)) {
                // One component was released. Was the whole thing active before?
                // Hard to tell without full previous state.
                // But if we assume it was active, then releasing any component releases the composite.
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
        // This is tricky for events. Usually composites aren't directly triggered by a single event
        // in a way that gives a clear value, but we can return 1.0 if the event completes it.
        // But we don't have access to the full state here easily.
        return 0F; 
    }
}
