package ca.nodeengine.quantum;

import ca.nodeengine.quantum.api.InputCode;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * Default implementation of {@link InputCode}
 *
 * @param code The int code of the input. E.x. scancode, button id, etc.
 * @param name The name of the input. {@code null} if the backend implementation doesn't provide a name.
 *
 * @author FX
 */
public record DefaultInputCode(int code, @Nullable String name) implements InputCode {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultInputCode defaultInputCode)) {
            return false;
        }
        return code == defaultInputCode.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public @NonNull String toString() {
        return name + " (" + code + ")";
    }
}
