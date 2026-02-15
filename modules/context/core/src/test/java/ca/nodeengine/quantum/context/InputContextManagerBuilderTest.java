package ca.nodeengine.quantum.context;

import ca.nodeengine.quantum.action.DefaultActionMap;
import ca.nodeengine.quantum.api.context.InputContextManager;
import ca.nodeengine.quantum.api.context.InputContextManagerBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link InputContextManagerBuilder}
 *
 * @author FX
 */
class InputContextManagerBuilderTest {

    @Test
    void testBuilderCreation() {
        InputContextManagerBuilder builder = InputContextManagerBuilder.create();
        assertNotNull(builder);
        assertTrue(builder instanceof DefaultInputContextManagerBuilder);
    }

    @Test
    void testBuildWithNamedContexts() {
        InputContextManager manager = InputContextManagerBuilder.create()
                .addContext("UI", new DefaultActionMap(), 100)
                .addContext("Game", new DefaultActionMap(), 0)
                .build();

        assertNotNull(manager);
        // By default they are only registered, not active
        assertTrue(manager.getActiveContexts().isEmpty());

        manager.pushContext("UI");
        assertEquals(1, manager.getActiveContexts().size());
        assertEquals("UI", manager.getActiveContexts().getFirst().name());
    }
}
