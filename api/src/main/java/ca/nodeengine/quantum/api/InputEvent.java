package ca.nodeengine.quantum.api;

/**
 * Represent raw device events coming from OS / windowing libs. (Low-Level Input Events)
 */
public interface InputEvent {
    long timestamp(); // nanoseconds or millis
}
