package ca.nodeengine.quantum.api.action;

/**
 * Implement this interface to receive action events.
 *
 * @author FX
 */
@FunctionalInterface
public interface ActionListener {

    /**
     * Called when an action event occurs.
     *
     * @param event The action event.
     */
    void onActionEvent(ActionEvent event);
}
