package ca.nodeengine.quantum.context;

import ca.nodeengine.quantum.api.context.InputContext;
import ca.nodeengine.quantum.api.context.InputContextManager;

import java.util.*;

/**
 * Default implementation of {@link InputContextManager}
 *
 * @author FX
 */
public class DefaultInputContextManager implements InputContextManager {
    private final Map<String, InputContext> registeredContexts = new HashMap<>();
    private final List<InputContext> activeContexts = new ArrayList<>();
    private final List<InputContext> sortedActiveContexts = new ArrayList<>();
    private float activationThreshold = 0.5F;

    @Override
    public float getActivationThreshold() {
        return activationThreshold;
    }

    @Override
    public void setActivationThreshold(float activationThreshold) {
        this.activationThreshold = activationThreshold;
    }

    @Override
    public void pushContext(InputContext context) {
        registeredContexts.put(context.name(), context);
        context.actionMap().setActivePredicate(() -> sortedActiveContexts.contains(context));
        activeContexts.add(context);
        updateSortedContexts();
    }

    @Override
    public void popContext(String name) {
        activeContexts.removeIf(c -> c.name().equals(name));
        updateSortedContexts();
    }

    @Override
    public void pushContext(String name) {
        InputContext context = registeredContexts.get(name);
        if (context == null) {
            throw new IllegalArgumentException("Context not registered: " + name);
        }
        if (!activeContexts.contains(context)) {
            activeContexts.add(context);
            updateSortedContexts();
        }
    }

    @Override
    public List<InputContext> getActiveContexts() {
        return Collections.unmodifiableList(sortedActiveContexts);
    }

    private void updateSortedContexts() {
        sortedActiveContexts.clear();
        sortedActiveContexts.addAll(activeContexts);
        // Sort by priority descending
        sortedActiveContexts.sort(Comparator.comparingInt(InputContext::priority).reversed());
    }

    public void registerContext(InputContext context) {
        registeredContexts.put(context.name(), context);
        context.actionMap().setActivePredicate(() -> sortedActiveContexts.contains(context));
    }
}
