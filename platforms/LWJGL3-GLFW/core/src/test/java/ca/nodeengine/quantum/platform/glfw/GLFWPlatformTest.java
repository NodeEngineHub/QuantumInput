package ca.nodeengine.quantum.platform.glfw;

import ca.nodeengine.quantum.api.InputSystem;
import ca.nodeengine.quantum.api.platform.glfw.GLFWPlatform;
import ca.nodeengine.quantum.api.state.GlobalInputState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link GLFWPlatform}
 *
 * @author FX
 */
public class GLFWPlatformTest {
    @Test
    public void testPlatformInstantiation() {
        try (DefaultGLFWPlatform platform = new DefaultGLFWPlatform()) {
            assertNotNull(platform);
        }
    }

    @Test
    public void testIsSupported() {
        try (DefaultGLFWPlatform platform = new DefaultGLFWPlatform()) {
            try {
                boolean supported = platform.isSupported();
                System.out.println("GLFW Supported: " + supported);
            } catch (Throwable t) {
                fail("isSupported() crashed: " + t.getMessage());
            }
        }
    }

    @Test
    public void testGlfwGlobalDevice() {
        try (InputSystem<GlobalInputState> inputSystem = InputSystem.createGlobalInputSystem()) {
            GLFWPlatform platform = inputSystem.getPlatform(GLFWPlatform.class);
            assertNotNull(platform);
            try {
                boolean supported = platform.isSupported();
                System.out.println("GLFW Supported: " + supported);
            } catch (Throwable t) {
                fail("isSupported() crashed: " + t.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
