# QuantumInput Core

The `core` module provides the default implementation of the QuantumInput API. It handles the discovery of platforms, processing of input events, and maintenance of the input state.

## Key Classes

- **`DefaultInputSystem`**: The primary implementation of `InputSystem`. It uses `ServiceLoader` to find available `QuantumPlatform` implementations.
- **`DefaultInputProcessor`**: Processes `InputEvent`s and updates the `MutableInputState`.
- **`DefaultGlobalInputState`**: Default implementation of `GlobalInputState`.
- **`DefaultPerDeviceInputState`**: Default implementation of `PerDeviceInputState`.

## Usage

In most cases, you don't need to interact with the `core` classes directly. Instead, use the factory methods in the `api` module, which will automatically use the `core` implementations if they are on the classpath.

### Automatic Initialization

```java
// This will return a DefaultInputSystem instance
InputSystem<GlobalInputState> inputSystem = InputSystem.createGlobalInputSystem();
// or for per-device platforms
InputSystem<PerDeviceInputState> inputSystem = InputSystem.createPerDeviceInputSystem();
```

### Manual Initialization (Optional)

If you need to provide a custom state or processor:

```java
DefaultGlobalInputState customState = new DefaultGlobalInputState();
InputSystem<GlobalInputState> inputSystem = new DefaultInputSystem<>(customState);
```

## How it works

1.  **Platform Discovery**: `DefaultInputSystem` searches for `QuantumPlatform` implementations using `ServiceLoader`.
2.  **Initialization**: Each discovered platform is initialized and registered with the `InputSystem`.
3.  **Event Pipeline**:
    -   Platforms produce `InputEvent`s.
    -   `DefaultInputSystem` receives these events and passes them to its `InputProcessor`.
    -   The `InputProcessor` updates the internal `InputState`.
    -   The events are then dispatched to any registered `InputListener`s.
4.  **Update Loop**: When `inputSystem.update()` is called, it triggers `update()` on all registered platforms.
