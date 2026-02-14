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
        ServiceLoader<QuantumPlatform> loader = QuantumPlatform.createServiceLoader();
        Optional<QuantumPlatform> mockPlatform = loader.stream()
                .map(ServiceLoader.Provider::get)
                .filter(p -> p instanceof MockPlatform)
                .findFirst();

        assertTrue(mockPlatform.isPresent(), "MockPlatform should be loaded via ServiceLoader");
    }

    @Test
    public void testInputSystemLoading() {
        ServiceLoader<InputSystem> loader = InputSystem.createServiceLoader();
        Optional<InputSystem> inputSystem = loader.findFirst();

        assertTrue(inputSystem.isPresent(), "InputSystem should be loaded via ServiceLoader");
        assertTrue(inputSystem.get() instanceof DefaultInputSystem, "Loaded InputSystem should be DefaultInputSystem");
    }

    @Test
    public void testInputSystemUsesPlatforms() {
        MockPlatform.ANY_INITIALIZED = false;
        MockPlatform.ANY_POLLED = false;

        InputSystem inputSystem = InputSystem.createServiceLoader().findFirst().orElseThrow();
        assertTrue(MockPlatform.ANY_INITIALIZED, "At least one MockPlatform should have been initialized");

        inputSystem.poll();
        assertTrue(MockPlatform.ANY_POLLED, "At least one MockPlatform should have been polled");
    }

    @Test
    public void testWindowRegistration() {
        MockPlatform.LAST_REGISTERED_WINDOW = -1;
        MockPlatform.LAST_UNREGISTERED_WINDOW = -1;

        InputSystem inputSystem = InputSystem.createServiceLoader().findFirst().orElseThrow();
        
        long testWindow = 12345L;
        inputSystem.registerWindow(testWindow);
        assertEquals(testWindow, MockPlatform.LAST_REGISTERED_WINDOW, "Window should be registered on MockPlatform");

        inputSystem.unregisterWindow(testWindow);
        assertEquals(testWindow, MockPlatform.LAST_UNREGISTERED_WINDOW, "Window should be unregistered from MockPlatform");
    }
}
