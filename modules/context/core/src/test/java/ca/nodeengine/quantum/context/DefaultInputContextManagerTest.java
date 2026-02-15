package ca.nodeengine.quantum.context;

import ca.nodeengine.quantum.api.context.InputContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link DefaultInputContextManager}
 *
 * @author FX
 */
class DefaultInputContextManagerTest {

    @Test
    void pushAndPopContext() {
        DefaultInputContextManager manager = new DefaultInputContextManager();
        InputContext ctx1 = new DefaultInputContext("UI", null, 100);
        InputContext ctx2 = new DefaultInputContext("Game", null, 0);

        manager.pushContext(ctx1);
        manager.pushContext(ctx2);

        List<InputContext> active = manager.getActiveContexts();
        assertEquals(2, active.size());
        assertEquals("UI", active.getFirst().name()); // Higher priority first

        manager.popContext("UI");
        active = manager.getActiveContexts();
        assertEquals(1, active.size());
        assertEquals("Game", active.getFirst().name());
    }

    @Test
    void prioritySorting() {
        DefaultInputContextManager manager = new DefaultInputContextManager();
        manager.pushContext(new DefaultInputContext("Low", null, 0));
        manager.pushContext(new DefaultInputContext("High", null, 100));
        manager.pushContext(new DefaultInputContext("Mid", null, 50));

        List<InputContext> active = manager.getActiveContexts();
        assertEquals("High", active.get(0).name());
        assertEquals("Mid", active.get(1).name());
        assertEquals("Low", active.get(2).name());
    }

    @Test
    void testActivateDeactivate() {
        DefaultInputContextManager manager = new DefaultInputContextManager();
        InputContext ctx1 = new DefaultInputContext("UI", null, 100);
        manager.registerContext(ctx1);

        assertTrue(manager.getActiveContexts().isEmpty());

        manager.pushContext("UI");
        assertEquals(1, manager.getActiveContexts().size());
        assertEquals("UI", manager.getActiveContexts().getFirst().name());

        manager.popContext("UI");
        assertTrue(manager.getActiveContexts().isEmpty());
    }

    @Test
    void testActivateNonRegisteredThrows() {
        DefaultInputContextManager manager = new DefaultInputContextManager();
        assertThrows(IllegalArgumentException.class, () -> manager.pushContext("Missing"));
    }
}
