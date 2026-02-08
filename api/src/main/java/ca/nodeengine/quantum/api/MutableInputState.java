package ca.nodeengine.quantum.api;

/**
 * Mutable version of {@link InputState}<br>
 * All input state implementations should extend this.
 * It's split for encapsulation to prevent keys from being set outside of quantum.
 */
public interface MutableInputState extends InputState {

    void setDown(InputCode code, boolean down);

    void setAxis(InputCode axis, float value);
}
