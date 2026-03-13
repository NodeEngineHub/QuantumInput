package ca.nodeengine.quantum;

import ca.nodeengine.quantum.event.InputEvent;
import ca.nodeengine.quantum.event.InputEventType;
import ca.nodeengine.quantum.event.InputListener;
import ca.nodeengine.quantum.platform.QuantumPlatform;
import ca.nodeengine.quantum.state.GlobalInputState;
import ca.nodeengine.quantum.state.PerDeviceInputState;
import ca.nodeengine.quantum.event.DefaultInputEvent;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link InputSystem.Builder}
 *
 * @author FX
 */
public class InputSystemBuilderTest {

    @Test
    public void testManualPlatformConfiguration() {
        MockGlobalPlatform platform = new MockGlobalPlatform();
        try (InputSystem<GlobalInputState> inputSystem = InputSystem.<GlobalInputState>builder()
                .withPlatform(platform)
                .build()) {
            
            assertNotNull(inputSystem);
            assertEquals(1, inputSystem.getPlatforms().size());
            assertTrue(inputSystem.getPlatforms().contains(platform));
            assertNotNull(inputSystem.state());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void testRemoveListener() {
        try (InputSystem<?> inputSystem = InputSystem.builder().discoverPlatforms().build()) {
            boolean[] called = {false};
            InputListener listener = event -> called[0] = true;
            InputEvent dummyEvent = new DefaultInputEvent(
                    null, InputEventType.KEY_PRESSED, 0, 0, 0);
            
            inputSystem.addListener(listener);
            ((InputListener)inputSystem).onInputEvent(dummyEvent);
            assertTrue(called[0]);
            
            called[0] = false;
            inputSystem.removeListener(listener);
            ((InputListener)inputSystem).onInputEvent(dummyEvent);
            assertFalse(called[0]);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void testBuildGlobal() {
        MockPlatform platform = new MockPlatform(false);
        try (InputSystem<GlobalInputState> inputSystem = InputSystem.<GlobalInputState>builder()
                .withPlatform(platform)
                .buildGlobal()) {
            
            assertNotNull(inputSystem);
            GlobalInputState state = inputSystem.state();
            assertNotNull(state);
            
            InputDevice device = new MockDevice("TestDevice");
            ((InputListener)inputSystem).onInputEvent(
                    new DefaultInputEvent(device, InputEventType.KEY_PRESSED, 65, 0, 0)
            );
            
            assertTrue(state.isKeyPressed(65));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void testBuildPerDevice() {
        MockPlatform platform = new MockPlatform(true);
        try (InputSystem<PerDeviceInputState> inputSystem = InputSystem.<PerDeviceInputState>builder()
                .withPlatform(platform)
                .buildPerDevice()) {
            
            assertNotNull(inputSystem);
            PerDeviceInputState state = inputSystem.state();
            assertNotNull(state);
            
            InputDevice device = new MockDevice("TestDevice");
            ((InputListener)inputSystem).onInputEvent(
                    new DefaultInputEvent(device, InputEventType.KEY_PRESSED, 65, 0, 0)
            );
            
            assertTrue(state.isKeyPressed(device, 65));
        } catch (Exception e) {
            fail(e);
        }
    }


    private record MockDevice(String name) implements InputDevice {

        @Override
        public boolean isGlobal() {
            return false;
        }
    }

    private record MockPlatform(boolean global) implements QuantumPlatform {

        @Override
        public Class<?> getApiClass() {
            return MockPlatform.class;
        }

        @Override
        public boolean isSupported() {
            return true;
        }

        @Override
        public boolean usesGlobalDevice() {
            return global;
        }

        @Override
        public Collection<InputDevice> getInputDevices() {
            return Collections.emptyList();
        }

        @Override
        public void initialize(InputListener listener) {
        }

        @Override
        public void update() {
        }

        @Override
        public void close() {
        }
    }
}
