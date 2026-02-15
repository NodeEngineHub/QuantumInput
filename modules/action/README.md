# QuantumInput Action Module

The Action module provides a way to decouple high-level application actions (like "Jump", "Fire", "MoveForward") from low-level physical inputs (like `KEY_SPACE`, `BUTTON_LEFT`, `AXIS_Y`).

## Key Concepts

- **Action Name**: A simple string identifier for an action.
- **`ActionBinding`**: Defines how a physical input maps to an action.
- **`ActionMap`**: A collection of bindings that define a particular input scheme.
- **`ActionInput`**: Interface for querying the state of actions.

## Usage

### 1. Creating an Action Map

Use the `ActionMapBuilder` to define your input mappings.

```java
ActionMap gameActions = ActionMapBuilder.create()
    .add("Jump", GLFW_KEY_SPACE)
    .add("Fire", GLFW_MOUSE_BUTTON_LEFT)
    .add("MoveForward", GLFW_KEY_W)
    .build();
```

### 2. Actions Event Triggers

You can use the `ActionEventBridge` to listen for action events instead of polling.

```java
gameActions.addListener(event -> {
    if (event.isActive()) {
        System.out.println("Action triggered: " + event.action());
    }
});

inputSystem.addListener(gameActions.createInputListener()); // Should only be done once per action map
```

### 3. Modifying Action Maps

You can modify an `ActionMap` to rebind actions.

```java
// Clear existing bindings for "Jump"
gameActions.clearBindings("Jump");

// Add a new binding
gameActions.addBinding(new DigitalBinding(null, "Jump", GLFW_KEY_ENTER));
```

### 4. Querying Action State

While you can use `ActionMap` directly to check bindings, it is often used in conjunction with the `context` module for prioritized resolution. 

If you want to query actions directly from a map:

```java
float value = gameActions.getBindings("Jump").get(0).value(inputState);
```

However, for most applications, it is recommended to use the `ContextActionInput` from the `context` module.

## Module Structure

- **`api`**: Defines the interfaces for bindings, maps, and builders.
- **`core`**: Provides default implementations like `DefaultActionMap` and `DigitalBinding`.
