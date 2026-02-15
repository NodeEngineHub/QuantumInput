# QuantumInput LWJGL3-GLFW Platform

This platform provides support for GLFW through LWJGL3, which is the standard for most modern OpenGL/Vulkan applications in Java.

## Usage

The GLFW platform is automatically discovered by the `InputSystem` via `ServiceLoader`.

### Registering Windows

Unlike some other platforms, GLFW requires you to register the window handles you want to monitor for input.

```java
InputSystem<GlobalInputState> inputSystem = InputSystem.createGlobalInputSystem();
GLFWPlatform glfwPlatform = inputSystem.getPlatform(GLFWPlatform.class);

if (glfwPlatform != null) {
    long windowHandle = ...; // Your GLFW window handle
    glfwPlatform.registerWindow(windowHandle);
}
```

## Features

- High-performance keyboard and mouse polling.
- Support for multiple windows.
- Integration with LWJGL3 GLFW bindings.
