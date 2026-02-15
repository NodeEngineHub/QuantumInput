package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.action.ActionMap;
import ca.nodeengine.quantum.api.action.ActionMapBuilder;
import ca.nodeengine.quantum.api.action.InputAction;
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
        
        InputAction jump = actionMap.getAction("Jump");
        assertNotNull(jump);
        assertEquals("Jump", jump.name());
        assertEquals(1, actionMap.getBindings(jump).size());

        InputAction fire = actionMap.getAction("Fire");
        assertNotNull(fire);
        assertEquals("Fire", fire.name());
        assertEquals(1, actionMap.getBindings(fire).size());
    }
}
