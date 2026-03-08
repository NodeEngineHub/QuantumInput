package ca.nodeengine.quantum.platform.javafx;

import ca.nodeengine.quantum.platform.QuantumPlatform;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link JavaFXPlatform}
 *
 * @author FX
 */
public class JavaFXPlatformTest {

    @Test
    void testPlatformDiscovery() {
        ServiceLoader<QuantumPlatform> loader = ServiceLoader.load(QuantumPlatform.class);
        boolean found = false;
        for (QuantumPlatform platform : loader) {
            if (platform instanceof JavaFXPlatform) {
                found = true;
                break;
            }
        }
        assertTrue(found, "JavaFXPlatform should be discovered via ServiceLoader");
    }

    @Test
    void testIsSupported() {
        try (DefaultJavaFXPlatform platform = new DefaultJavaFXPlatform()) {
            // Since we are running in an environment where JavaFX should be on the classpath (via Gradle)
            assertTrue(platform.isSupported(), "JavaFXPlatform should be supported if JavaFX is on the classpath");
        }
    }
}
