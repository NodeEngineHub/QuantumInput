package ca.nodeengine.quantum;

import ca.nodeengine.quantum.api.InputSystem;
import ca.nodeengine.quantum.api.platform.QuantumPlatform;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceLoaderTest {

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
            assertNotNull(inputSystem.getPlatform(MockPlatform.class), "MockPlatform should be loaded via ServiceLoader");
        } catch (Exception e) {
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
}
