package ca.nodeengine.quantum;

import ca.nodeengine.quantum.action.DefaultActionInput;
import ca.nodeengine.quantum.api.InputEventListener;
import ca.nodeengine.quantum.api.InputState;
import ca.nodeengine.quantum.api.InputSystem;
import ca.nodeengine.quantum.api.action.ActionInput;
import ca.nodeengine.quantum.api.context.InputContextManager;
import ca.nodeengine.quantum.context.DefaultInputContextManager;

public class QuantumInput implements InputSystem {
    private final DefaultInputState state;
    private final DefaultInputProcessor processor;
    private final DefaultInputContextManager contextManager;
    private final DefaultActionInput actionInput;

    public QuantumInput() {
        this.state = new DefaultInputState();
        this.processor = new DefaultInputProcessor(this.state);
        this.contextManager = new DefaultInputContextManager();
        this.actionInput = new DefaultActionInput(this.state, this.contextManager);
    }

    @Override
    public void poll() {
        state.poll();
    }

    public void updateActions() {
        actionInput.update();
    }

    @Override
    public ActionInput actions() {
        return actionInput;
    }

    @Override
    public InputState rawState() {
        return state;
    }

    @Override
    public InputContextManager contexts() {
        return contextManager;
    }

    public InputEventListener eventListener() {
        return processor;
    }
}
