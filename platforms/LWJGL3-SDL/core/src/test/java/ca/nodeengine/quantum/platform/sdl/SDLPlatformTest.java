package ca.nodeengine.quantum.platform.sdl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link SDLPlatform}
 *
 * @author FX
 */
public class SDLPlatformTest {
    @Test
    public void testPlatformInstantiation() {
        try (DefaultSDLPlatform platform = new DefaultSDLPlatform()) {
            assertNotNull(platform);
        }
    }

    @Test
    public void testIsSupported() {
        try (DefaultSDLPlatform platform = new DefaultSDLPlatform()) {
            try {
                boolean supported = platform.isSupported();
                System.out.println("SDL Supported: " + supported);
            } catch (Throwable t) {
                fail("isSupported() crashed: " + t.getMessage());
            }
        }
    }

    @Test
    public void testRegistration() {
        try (DefaultSDLPlatform platform = new DefaultSDLPlatform()) {
            platform.registerWindow(1L);
            platform.unregisterWindow(1L);
        }
    }
}
