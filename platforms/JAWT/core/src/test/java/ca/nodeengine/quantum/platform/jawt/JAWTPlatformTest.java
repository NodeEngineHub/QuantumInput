package ca.nodeengine.quantum.platform.jawt;

import ca.nodeengine.quantum.api.platform.QuantumPlatform;
import ca.nodeengine.quantum.api.platform.jawt.JAWTPlatform;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link JAWTPlatform}
 *
 * @author FX
 */
public class JAWTPlatformTest {

    @Test
    void testPlatformDiscovery() {
        ServiceLoader<QuantumPlatform> loader = ServiceLoader.load(QuantumPlatform.class);
        boolean found = false;
        for (QuantumPlatform platform : loader) {
            if (platform instanceof JAWTPlatform) {
                found = true;
                break;
            }
        }
        assertTrue(found, "JAWTPlatform should be discovered via ServiceLoader");
    }

    @Test
    void testIsSupported() {
        try (DefaultJAWTPlatform platform = new DefaultJAWTPlatform()) {
            assertTrue(platform.isSupported(), "JAWTPlatform should be supported in standard Java environment");
        }
    }
}
