package ca.nodeengine.quantum.platform.glfw;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GLFWPlatformTest {
    @Test
    public void testPlatformInstantiation() {
        GLFWPlatform platform = new GLFWPlatform();
        assertNotNull(platform);
    }

    @Test
    public void testIsSupported() {
        GLFWPlatform platform = new GLFWPlatform();
        try {
            boolean supported = platform.isSupported();
            System.out.println("GLFW Supported: " + supported);
        } catch (Throwable t) {
            fail("isSupported() crashed: " + t.getMessage());
        }
    }
}
