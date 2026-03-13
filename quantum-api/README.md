# QuantumInput API

The `api` module defines the core interfaces and abstractions for the QuantumInput system. It is designed to be platform-agnostic, allowing developers to write input logic that works across different hardware backends.

## Key Interfaces

- **`InputSystem`**: The main entry point for the library. It manages platforms, processors, and listeners.
- **`InputState`**: Represents the queryable state of inputs (keys, buttons, axes).
- **`GlobalInputState`**: A specialized `InputState` for platforms that use global inputs.
- **`PerDeviceInputState`**: A specialized `InputState` for platforms that use per-device inputs.
- **`QuantumPlatform`**: Interface for hardware-specific platform implementations (e.g., GLFW, JInput).
- **`InputListener`**: Interface for event-based input handling.

## Usage

### Obtaining an Input System

You can create an `InputSystem` instance using the provided static factory methods, which use `ServiceLoader` to find an implementation (typically provided by the `core` module).

```java
// Create a system for global input
InputSystem<GlobalInputState> inputSystem = InputSystem.createGlobalInputSystem();

// Or for per-device input
InputSystem<PerDeviceInputState> inputSystem = InputSystem.createPerDeviceInputSystem();
```
Whether the input state is global or per-device is based on which platforms you are using.  
However, you can use either type in your project. The system will handle the conversion.

### Polling Input State

<details>
<summary>Global</summary>

```java
inputSystem.update();
GlobalInputState state = inputSystem.state();

if (state.isKeyPressed(GLFW_KEY_SPACE)) {
    // Jump!
}
```
</details>

<br>

<details>
<summary>Per-Device</summary>

```java
inputSystem.update();
SomePerDevicePlatform platform = inputSystem.getPlatform(SomePerDevicePlatform.class);
PerDeviceInputState state = inputSystem.state();

if (state.isKeyPressed(platform.getKeyboard(0), platform.KEY_SPACE)) {
    // Jump!
}
```
Per-Device platforms let you select which keyboard/device you want to query specifically.<br>
The exact device retrieval method depends on the platforms' implementation.
</details>

### Event-Based Input

```java
inputSystem.addListener(event -> {
    if (event.type() == InputEventType.KEY_PRESSED) {
        System.out.println("Key pressed: " + event.getCode());
    }
});
```

<details>
<summary>Per-Device Extension</summary>

Per-Device platforms will also provide the device within the event. Allowing you to filter by device.  
```java
inputSystem.addListener(event -> {
    if (event.type() == InputEventType.KEY_PRESSED && 
            event.device() instanceof SomeCoolDevice someCoolDevice && someCoolDevice.isEnabled()) {
        System.out.println("Key pressed: " + event.getCode());
    }
});
```
</details>
<br>
