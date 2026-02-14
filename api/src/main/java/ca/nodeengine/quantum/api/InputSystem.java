package ca.nodeengine.quantum.api;

/**
 * This is what most engine users touch. (High-Level Input System Facade)
 *
 * TODO: Move explanation to the wiki
 * Why are we not using a global input state like other game engines, or even a player-based state?
 * <ul>
 *   <li>Godot:
 *     <ul>
 *         <li>{@code Input.is_key_pressed(Key)}</li>
 *         <li>{@code Input.is_action_pressed(String)}</li>
 *     </ul>
 *  </li>
 *  <li>Unity:
 *      <ul>
 *          <li>{@code Input.GetKeyDown(Key)} (old system)</li>
 *          <li>{@code inputAction.action.ApplyBindingOverride(InputBinding)} (new player based system)</li>
 *      </ul>
 *  </li>
 *  <li>Love2d:
 *      <ul>
 *          <li>{@code love.keyboard.isDown(Key)}</li>
 *      </ul>
 *  </li>
 *  <li>Unreal:
 *      <ul>
 *          <li>{@code InputComponent->BindAction(String, Func, UserClass, Func);} (Player Input Component)</li>
 *      </ul>
 *  </li>
 * </ul>
 *
 * Well there are multiple reasons:
 * 1. Abstraction. Developers should be able to choose how their inputs are handled.
 *    If they want to make it global, they can. See the QuantumInput-global module.
 *    Dev's shouldn't be required to implement a system a certain why.
 *    This is why we offer different modules for the different ways you may want to use the system,
 *    while still having all the modules work together without issues.
 *    This is only possible due to the abstractions created.
 * 2. Multi-Usage. Having a global state or player bound state limits your usage of a system.
 *    Example: If you want to run many tests at the same time,
 *    without needing to have multiple instances of the game running.
 *    You can do that, since you can have multiple instances of the input system.
 *    A global system wouldn't allow this, and a player bound system isn't easy to run tests with.
 */
public interface InputSystem {
    void poll();

    InputState rawState();
    
    /**
     * Registers a window with any platform that supports it.
     *
     * @param windowHandle the handle of the window to register
     */
    void registerWindow(long windowHandle);

    /**
     * Unregisters a window from any platform that supports it.
     *
     * @param windowHandle the handle of the window to unregister
     */
    void unregisterWindow(long windowHandle);

    /**
     * Creates a {@link java.util.ServiceLoader} for {@link InputSystem}.
     *
     * @return a service loader for InputSystem
     */
    static java.util.ServiceLoader<InputSystem> createServiceLoader() {
        return java.util.ServiceLoader.load(InputSystem.class);
    }
}
