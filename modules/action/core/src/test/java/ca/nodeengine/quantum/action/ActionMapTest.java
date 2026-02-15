package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.action.ActionMap;
import ca.nodeengine.quantum.api.action.ActionMapSerializer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ActionMap}
 *
 * @author FX
 */
class ActionMapTest {

    @Test
    void testBuilder() {
        ActionMap actionMap = ActionMap.create()
                .add("Jump", 32) // space
                .add("Fire", 1); // left

        assertNotNull(actionMap);

        assertEquals(1, actionMap.getBindings("Jump").size());
        assertEquals(1, actionMap.getBindings("Fire").size());
    }

    @Test
    void testFluentBuilder() {
        ActionMap actionMap = ActionMap.create()
                .bind("Jump").toKey(32) // space
                .bind("Fire").toButton(1) // left
                .bind("Save").with(341).toKey(83) // control + s
                .bind("MoveX").deadzone(0.1F).scale(2.0F).toAxis(0);

        assertNotNull(actionMap);
        assertEquals(1, actionMap.getBindings("Jump").size());
        assertEquals(1, actionMap.getBindings("Fire").size());
        assertEquals(1, actionMap.getBindings("Save").size());
        assertEquals(1, actionMap.getBindings("MoveX").size());

        // Check if "Save" is a composite binding
        assertTrue(actionMap.getBindings("Save").get(0) instanceof CompositeBinding);
        // Check if "MoveX" is an axis binding
        assertTrue(actionMap.getBindings("MoveX").get(0) instanceof AxisBinding);
    }

    @Test
    void testSerialization() {
        ActionMap actionMap = ActionMap.create()
                .bind("Jump").toKey(32) // space
                .bind("MoveX").deadzone(0.15F).scale(1.5F).toAxis(0);

        List<Map<String, Object>> exported = ActionMapSerializer.exportBindings(actionMap);
        assertEquals(2, exported.size());

        ActionMap importedMap = ActionMap.create();
        ActionMapSerializer.importBindings(importedMap, exported);

        assertEquals(1, importedMap.getBindings("Jump").size());
        assertEquals(1, importedMap.getBindings("MoveX").size());

        AxisBinding axisBinding = (AxisBinding) importedMap.getBindings("MoveX").get(0);
        assertEquals(0.15F, axisBinding.deadzone(), 0.001F);
        assertEquals(1.5F, axisBinding.scale(), 0.001F);
    }
}
