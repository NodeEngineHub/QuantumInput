package ca.nodeengine.quantum.platform.jogl;

import ca.nodeengine.quantum.platform.QuantumPlatform;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link JOGLPlatform}
 *
 * @author FX
 */
public class JOGLPlatformTest {

    @Test
    void testPlatformDiscovery() {
        ServiceLoader<QuantumPlatform> loader = ServiceLoader.load(QuantumPlatform.class);
        boolean found = false;
        for (QuantumPlatform platform : loader) {
            if (platform instanceof JOGLPlatform) {
                found = true;
                break;
            }
        }
        assertTrue(found, "JOGLPlatform should be discovered via ServiceLoader");
    }
}
