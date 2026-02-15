package ca.nodeengine.quantum;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to make sure that we accurately detect state transitions
 *
 * @author FX
 */
class StateTransitionTest {

    @Test
    void testGlobalStateTransitions() {
        DefaultGlobalInputState state = new DefaultGlobalInputState();
        int KEY_A = 65;

        // Frame 1: Press A
        state.setKey(null, KEY_A, true);
        assertTrue(state.isKeyPressed(KEY_A), "Key should be pressed in first frame");
        assertTrue(state.isKeyHeld(KEY_A), "Key should be held");
        assertFalse(state.isKeyReleased(KEY_A), "Key should not be released");

        // Frame 1 End
        state.update();

        // Frame 2: Still holding A
        assertFalse(state.isKeyPressed(KEY_A), "Key should NOT be pressed in second frame");
        assertTrue(state.isKeyHeld(KEY_A), "Key should still be held");
        assertFalse(state.isKeyReleased(KEY_A), "Key should not be released");

        // Frame 2 End
        state.update();

        // Frame 3: Release A
        state.setKey(null, KEY_A, false);
        assertFalse(state.isKeyPressed(KEY_A), "Key should not be pressed");
        assertFalse(state.isKeyHeld(KEY_A), "Key should not be held");
        assertTrue(state.isKeyReleased(KEY_A), "Key should be released");

        // Frame 3 End
        state.update();

        // Frame 4: After release
        assertFalse(state.isKeyPressed(KEY_A));
        assertFalse(state.isKeyHeld(KEY_A));
        assertFalse(state.isKeyReleased(KEY_A), "Key release state should be cleared after update");
    }
}
