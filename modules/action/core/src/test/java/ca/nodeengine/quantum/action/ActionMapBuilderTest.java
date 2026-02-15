package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.action.ActionMap;
import ca.nodeengine.quantum.api.action.ActionMapBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionMapBuilderTest {

    @Test
    void testBuilder() {
        ActionMap actionMap = ActionMapBuilder.create()
                .add("Jump", 32)
                .add("Fire", 1)
                .build();

        assertNotNull(actionMap);

        assertEquals(1, actionMap.getBindings("Jump").size());
        assertEquals(1, actionMap.getBindings("Fire").size());
    }
}
