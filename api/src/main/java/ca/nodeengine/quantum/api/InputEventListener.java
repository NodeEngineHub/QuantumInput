package ca.nodeengine.quantum.api;

/**
 * Something must receive events from the platform layer. (Input Event Sink)
 * Platform adapters pushes into this.
 */
public interface InputEventListener {
    void onInputEvent(InputEvent event);
}
