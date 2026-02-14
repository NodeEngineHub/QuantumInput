package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.action.InputAction;
import org.jspecify.annotations.NonNull;

public record DefaultInputAction(String name) implements InputAction {

    @Override
    public @NonNull String toString() {
        return "InputAction[" + name + "]";
    }
}
