package ca.nodeengine.quantum.api;

import ca.nodeengine.quantum.api.action.ActionInput;
import ca.nodeengine.quantum.api.context.InputContextManager;

/**
 * This is what most engine users touch. (High-Level Input System Facade)
 */
public interface InputSystem {

    void poll();

    ActionInput actions();

    InputState rawState();

    InputContextManager contexts();
}
