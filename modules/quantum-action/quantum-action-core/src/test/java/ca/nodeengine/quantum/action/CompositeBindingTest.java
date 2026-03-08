package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.InputType;
import ca.nodeengine.quantum.state.GlobalInputState;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link CompositeBinding}
 *
 * @author FX
 */
class CompositeBindingTest {

    @Test
    void testCompositeBinding() {
        MockState state = new MockState();
        int KEY_CTRL = 17;
        int KEY_S = 83;

        DigitalBinding ctrl = new DigitalBinding(null, "Ctrl", KEY_CTRL, InputType.KEY);
        DigitalBinding s = new DigitalBinding(null, "S", KEY_S, InputType.KEY);
        CompositeBinding ctrlS = new CompositeBinding("Save", ctrl, s);

        // Frame 1: Press Ctrl
        state.down.add(KEY_CTRL);
        state.pressed.add(KEY_CTRL);
        assertFalse(ctrlS.matches(state));
        assertFalse(ctrlS.isPressed(state));

        state.advance();

        // Frame 2: Press S while holding Ctrl
        state.down.add(KEY_S);
        state.pressed.add(KEY_S);
        assertTrue(ctrlS.matches(state));
        assertTrue(ctrlS.isPressed(state), "Composite should be pressed when second key is pressed");

        state.advance();

        // Frame 3: Holding both
        assertTrue(ctrlS.matches(state));
        assertFalse(ctrlS.isPressed(state), "Composite should not be pressed when already held");

        state.advance();

        // Frame 4: Release S
        state.down.remove(KEY_S);
        state.released.add(KEY_S);
        assertFalse(ctrlS.matches(state));
        assertTrue(ctrlS.isReleased(state), "Composite should be released when any component is released");

        state.advance();
        assertFalse(ctrlS.isReleased(state));
    }


    private static class MockState implements GlobalInputState {
        Set<Integer> down = new HashSet<>();
        Set<Integer> pressed = new HashSet<>();
        Set<Integer> released = new HashSet<>();

        void advance() {
            pressed.clear();
            released.clear();
        }

        @Override
        public boolean isKeyPressed(int code) {
            return pressed.contains(code);
        }

        @Override
        public boolean isKeyHeld(int code) {
            return down.contains(code);
        }

        @Override
        public boolean isKeyReleased(int code) {
            return released.contains(code);
        }

        @Override
        public boolean isButtonPressed(int code) {
            return false;
        }

        @Override
        public boolean isButtonHeld(int code) {
            return false;
        }

        @Override
        public boolean isButtonReleased(int code) {
            return false;
        }

        @Override
        public float getAxis(int axisCode) {
            return 0;
        }

        @Override
        public float[] getMouse() {
            return new float[2];
        }

        @Override
        public float[] getScroll() {
            return new float[2];
        }

        @Override
        public void update() {}

        @Override
        public void reset() {}
    }
}
