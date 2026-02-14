package ca.nodeengine.quantum.context;

import ca.nodeengine.quantum.api.context.InputContext;
import ca.nodeengine.quantum.api.context.InputContextManager;

import java.util.*;

public class DefaultInputContextManager implements InputContextManager {
    private final List<InputContext> contexts = new ArrayList<>();
    private final List<InputContext> sortedContexts = new ArrayList<>();

    @Override
    public void pushContext(InputContext context) {
        contexts.add(context);
        updateSortedContexts();
    }

    @Override
    public void popContext(String name) {
        contexts.removeIf(c -> c.name().equals(name));
        updateSortedContexts();
    }

    @Override
    public List<InputContext> getActiveContexts() {
        return Collections.unmodifiableList(sortedContexts);
    }

    private void updateSortedContexts() {
        sortedContexts.clear();
        sortedContexts.addAll(contexts);
        // Sort by priority descending
        sortedContexts.sort(Comparator.comparingInt(InputContext::priority).reversed());
    }
}
