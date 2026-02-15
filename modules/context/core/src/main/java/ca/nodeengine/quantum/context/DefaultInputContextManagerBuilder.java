package ca.nodeengine.quantum.context;

import ca.nodeengine.quantum.api.action.ActionMap;
import ca.nodeengine.quantum.api.context.InputContext;
import ca.nodeengine.quantum.api.context.InputContextManager;
import ca.nodeengine.quantum.api.context.InputContextManagerBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link InputContextManagerBuilder}
 *
 * @author FX
 */
public class DefaultInputContextManagerBuilder implements InputContextManagerBuilder {
    private final List<InputContext> contexts = new ArrayList<>();

    @Override
    public InputContextManagerBuilder addContext(InputContext context) {
        contexts.add(context);
        return this;
    }

    @Override
    public InputContextManagerBuilder addContext(String name, ActionMap actionMap, int priority) {
        return addContext(new DefaultInputContext(name, actionMap, priority));
    }

    @Override
    public InputContextManager build() {
        DefaultInputContextManager manager = new DefaultInputContextManager();
        for (InputContext context : contexts) {
            manager.registerContext(context);
        }
        return manager;
    }
}
