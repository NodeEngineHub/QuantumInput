package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.InputType;
import ca.nodeengine.quantum.api.state.GlobalInputState;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link OrBinding}
 *
 * @author FX
 */
class OrBindingTest {

    @Test
    void testOrBinding() {
        MockState state = new MockState();
        int KEY_A = 65;
        int KEY_B = 66;

        DigitalBinding a = new DigitalBinding(null, "A", KEY_A, InputType.KEY);
        DigitalBinding b = new DigitalBinding(null, "B", KEY_B, InputType.KEY);
        OrBinding or = new OrBinding("Action", a, b);

        // Initial state
        assertFalse(or.matches(state));

        // Press A
        state.down.add(KEY_A);
        state.pressed.add(KEY_A);
        assertTrue(or.matches(state));
        assertTrue(or.isPressed(state));

        state.advance();

        // Hold A
        assertTrue(or.matches(state));
        assertFalse(or.isPressed(state));

        // Press B while A is held
        state.down.add(KEY_B);
        state.pressed.add(KEY_B);
        assertTrue(or.matches(state));
        assertTrue(or.isPressed(state));

        state.advance();

        // Release A
        state.down.remove(KEY_A);
        state.released.add(KEY_A);
        assertTrue(or.matches(state), "Should still match because B is held");
        assertTrue(or.isReleased(state), "Should be released because one component was released");

        state.advance();

        // Release B
        state.down.remove(KEY_B);
        state.released.add(KEY_B);
        assertFalse(or.matches(state));
        assertTrue(or.isReleased(state));
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
