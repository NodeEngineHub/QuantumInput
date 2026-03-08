package ca.nodeengine.quantum.platform.input4j;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Input4jPlatform}
 *
 * @author FX
 */
public class Input4jPlatformTest {
    @Test
    public void testPlatformInstantiation() {
        try (DefaultInput4jPlatform platform = new DefaultInput4jPlatform()) {
            assertNotNull(platform);
        }
    }

    @Test
    public void testIsSupported() {
        try (DefaultInput4jPlatform platform = new DefaultInput4jPlatform()) {
            try {
                boolean supported = platform.isSupported();
                System.out.println("Input4j Supported: " + supported);
            } catch (Throwable t) {
                fail("isSupported() crashed: " + t.getMessage());
            }
        }
    }
}
