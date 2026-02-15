package ca.nodeengine.quantum.api.action;

/**
 * Action State Query API (What Gameplay Uses)
 * The public-facing input API.
 */
public interface ActionInput {
    boolean isDown(String action);

    float getValue(String action);
}
