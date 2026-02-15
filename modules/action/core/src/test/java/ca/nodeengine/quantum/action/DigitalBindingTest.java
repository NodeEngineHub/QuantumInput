package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.InputType;
import ca.nodeengine.quantum.api.state.GlobalInputState;
import ca.nodeengine.quantum.api.state.PerDeviceInputState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link DigitalBinding}
 *
 * @author FX
 */
class DigitalBindingTest {

    private final InputDevice device = new InputDevice() {
        @Override
        public String name() {
            return "TestDevice";
        }

        @Override
        public boolean isGlobal() {
            return false;
        }
    };

    private static final String action = "TestAction";

    @Test
    void matchesGlobalState() {
        DigitalBinding keyBinding = new DigitalBinding(device, action, 10, InputType.KEY);
        DigitalBinding buttonBinding = new DigitalBinding(device, action, 10, InputType.BUTTON);
        MockGlobalInputState state = new MockGlobalInputState();

        state.keyHeld = true;
        assertTrue(keyBinding.matches(state));
        assertFalse(buttonBinding.matches(state));

        state.keyHeld = false;
        state.buttonHeld = true;
        assertFalse(keyBinding.matches(state));
        assertTrue(buttonBinding.matches(state));

        state.buttonHeld = false;
        assertFalse(buttonBinding.matches(state));
    }

    @Test
    void matchesPerDeviceState() {
        DigitalBinding keyBinding = new DigitalBinding(device, action, 10, InputType.KEY);
        DigitalBinding buttonBinding = new DigitalBinding(device, action, 10, InputType.BUTTON);
        MockPerDeviceInputState state = new MockPerDeviceInputState();

        state.keyHeld = true;
        assertTrue(keyBinding.matches(state));
        assertFalse(buttonBinding.matches(state));

        state.keyHeld = false;
        state.buttonHeld = true;
        assertFalse(keyBinding.matches(state));
        assertTrue(buttonBinding.matches(state));

        state.buttonHeld = false;
        assertFalse(buttonBinding.matches(state));
    }

    @Test
    void valueReturnsOneWhenMatched() {
        DigitalBinding binding = new DigitalBinding(device, action, 10);
        MockGlobalInputState state = new MockGlobalInputState();
        state.keyHeld = true;
        assertEquals(1.0f, binding.value(state));
    }

    @Test
    void valueReturnsZeroWhenNotMatched() {
        DigitalBinding binding = new DigitalBinding(device, action, 10);
        MockGlobalInputState state = new MockGlobalInputState();
        state.keyHeld = false;
        state.buttonHeld = false;
        assertEquals(0.0f, binding.value(state));
    }

    static class MockGlobalInputState implements GlobalInputState {
        boolean keyHeld;
        boolean buttonHeld;

        @Override
        public boolean isKeyPressed(int code) {
            return false;
        }

        @Override
        public boolean isKeyHeld(int code) {
            return keyHeld;
        }

        @Override
        public boolean isKeyReleased(int code) {
            return false;
        }

        @Override
        public boolean isButtonPressed(int code) {
            return false;
        }

        @Override
        public boolean isButtonHeld(int code) {
            return buttonHeld;
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

    static class MockPerDeviceInputState implements PerDeviceInputState {
        boolean keyHeld;
        boolean buttonHeld;

        @Override
        public boolean isKeyPressed(InputDevice device, int code) {
            return false;
        }

        @Override
        public boolean isKeyHeld(InputDevice device, int code) {
            return keyHeld;
        }

        @Override
        public boolean isKeyReleased(InputDevice device, int code) {
            return false;
        }

        @Override
        public boolean isButtonPressed(InputDevice device, int code) {
            return false;
        }

        @Override
        public boolean isButtonHeld(InputDevice device, int code) {
            return buttonHeld;
        }

        @Override
        public boolean isButtonReleased(InputDevice device, int code) {
            return false;
        }

        @Override
        public float getAxis(InputDevice device, int axisCode) {
            return 0;
        }

        @Override
        public float[] getMouse(InputDevice device) {
            return new float[2];
        }

        @Override
        public float[] getScroll(InputDevice device) {
            return new float[2];
        }

        @Override
        public void update() {}

        @Override
        public void reset() {}
    }
}
