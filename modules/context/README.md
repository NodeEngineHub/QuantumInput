# QuantumInput Context Module

The Context module manages multiple `ActionMap`'s and determines which one should handle a specific action based on a prioritized stack. This is useful for handling different application states, such as switching between gameplay and menu inputs.  

> [!IMPORTANT]
> This module relies on the `action` module.

## Key Concepts

- **`InputContext`**: A named container for an `ActionMap` with an associated priority.
- **`InputContextManager`**: Manages a set of contexts and maintains a stack of active ones, providing context-aware action state resolution.

## Usage

### 1. Creating the Context Manager

Use the `InputContextManagerBuilder` to register your contexts.

```java
InputContextManager contextManager = InputContextManagerBuilder.create()
    .addContext("Game", gameActionMap, 0)
    .addContext("Menu", menuActionMap, 100)
    .build();
```

### 2. Managing the Context Stack

You can activate and deactivate contexts by name. Higher priority contexts will override lower priority ones if they define the same action.

```java
// Activate gameplay
contextManager.pushContext("Game");

// Open menu (overrides gameplay if it has higher priority)
contextManager.pushContext("Menu");

// Close menu
contextManager.popContext("Menu");
```

### 3. Querying Context-Aware Actions

Use `InputContextManager` to check the state of actions. It will look through the active contexts from highest priority to lowest to find a binding for the requested action.

```java
if (contextManager.isActive(inputState, "Jump")) {
    player.jump();
}
```

## Module Structure

- **`api`**: Defines the interfaces for contexts, managers, and builders.
- **`core`**: Provides default implementations.
