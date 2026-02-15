package ca.nodeengine.quantum.platform.jinput;

import ca.nodeengine.quantum.api.event.InputEvent;
import ca.nodeengine.quantum.api.event.InputListener;
import ca.nodeengine.quantum.api.platform.jinput.JInputPlatform;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link JInputPlatform}
 *
 * @author FX
 */
class JInputPlatformTest {

    @Test
    void testInitialization() {
        try (DefaultJInputPlatform platform = new DefaultJInputPlatform()) {
            List<InputEvent> events = new ArrayList<>();
            InputListener listener = events::add;

            // initialize should not throw exception even if no controllers are found
            assertDoesNotThrow(() -> platform.initialize(listener));

            // update should not throw exception
            assertDoesNotThrow(platform::update);

            // close should not throw exception
            assertDoesNotThrow(platform::close);
        }
    }

    @Test
    void testIsSupported() {
        try (DefaultJInputPlatform platform = new DefaultJInputPlatform()) {
            // This might return true or false depending on the environment, but it should not crash
            assertDoesNotThrow(platform::isSupported);
        }
    }
    
    @Test
    void testGetApiClass() {
        try (DefaultJInputPlatform platform = new DefaultJInputPlatform()) {
            assertEquals(JInputPlatform.class, platform.getApiClass());
        }
    }

    @Test
    void testGetInputDevices() {
        try (DefaultJInputPlatform platform = new DefaultJInputPlatform()) {
            platform.initialize(event -> {});
            assertNotNull(platform.getInputDevices());
            // Since we don't know if the test environment has controllers, we just check if it's not null.
        }
    }
}
