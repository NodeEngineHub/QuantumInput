package ca.nodeengine.quantum;

import ca.nodeengine.quantum.exception.QuantumInputException;
import ca.nodeengine.quantum.platform.QuantumPlatform;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Tests for platforms
 *
 * @author FX
 */
public class PlatformTest {

    @Test
    public void testPlatformLoading() {
        ServiceLoader<QuantumPlatform> loader = ServiceLoader.load(QuantumPlatform.class);
        Optional<QuantumPlatform> mockPlatform = loader.stream()
                .map(ServiceLoader.Provider::get)
                .filter(p -> p instanceof MockPlatform)
                .findFirst();

        assertTrue(mockPlatform.isPresent(), "MockPlatform should be loaded via ServiceLoader");
    }

    @Test
    public void testInputSystemLoading() {
        try(InputSystem<DefaultGlobalInputState> inputSystem = new DefaultInputSystem<>(
                new DefaultInputProcessor<>(new DefaultGlobalInputState())
        )) {
            assertNotNull(inputSystem.getPlatform(MockGlobalPlatform.class), "MockGlobalPlatform should be loaded via ServiceLoader");
            assertNotNull(inputSystem.getPlatform(MockPerDevicePlatform.class), "MockPerDevicePlatform should be loaded via ServiceLoader");
            assertFalse(inputSystem.state().isKeyHeld(1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testNotSupported() {
        MockPlatform.USE_PER_DEVICE_PLATFORM = false;
        try(InputSystem<DefaultGlobalInputState> inputSystem = new DefaultInputSystem<>(
                new DefaultInputProcessor<>(new DefaultGlobalInputState())
        )) {
            assertNotNull(inputSystem.getPlatform(MockGlobalPlatform.class), "MockGlobalPlatform should be loaded via ServiceLoader");
            assertNull(inputSystem.getPlatform(MockPerDevicePlatform.class), "MockPerDevicePlatform shouldn't be loaded via ServiceLoader");
        } catch (Exception e) {
            MockPlatform.USE_PER_DEVICE_PLATFORM = true;
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testInputSystemUsesPlatforms() {
        MockPlatform.ANY_INITIALIZED = false;
        MockPlatform.ANY_UPDATED = false;

        try(InputSystem<?> inputSystem = new DefaultInputSystem<>(
                new DefaultInputProcessor<>(new DefaultGlobalInputState())
        )) {
            assertTrue(MockPlatform.ANY_INITIALIZED, "At least one MockPlatform should have been initialized");

            inputSystem.update();
            assertTrue(MockPlatform.ANY_UPDATED, "At least one MockPlatform should have been updated");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testInputSystemPerDeviceCast() {
        MockPlatform.USE_PER_DEVICE_PLATFORM = true;

        assertThrows(QuantumInputException.class, InputSystem::createGlobalInputSystem);
        assertDoesNotThrow(() -> InputSystem.createPerDeviceInputSystem());
    }

    @Test
    public void testInputSystemGlobalCast() {
        MockPlatform.USE_PER_DEVICE_PLATFORM = false;

        assertDoesNotThrow(() -> InputSystem.createGlobalInputSystem());
        assertThrows(QuantumInputException.class, InputSystem::createPerDeviceInputSystem);
    }
}
