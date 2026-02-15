package ca.nodeengine.quantum;

import ca.nodeengine.quantum.api.InputSystem;
import ca.nodeengine.quantum.api.event.InputEvent;
import ca.nodeengine.quantum.api.event.InputEventType;
import ca.nodeengine.quantum.api.event.InputListener;
import ca.nodeengine.quantum.api.state.GlobalInputState;
import ca.nodeengine.quantum.event.DefaultInputEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link InputSystem.Builder}
 *
 * @author FX
 */
public class InputSystemBuilderTest {

    @Test
    public void testManualPlatformConfiguration() {
        MockGlobalPlatform platform = new MockGlobalPlatform();
        try (InputSystem<GlobalInputState> inputSystem = InputSystem.<GlobalInputState>builder()
                .withPlatform(platform)
                .build()) {
            
            assertNotNull(inputSystem);
            assertEquals(1, inputSystem.getPlatforms().size());
            assertTrue(inputSystem.getPlatforms().contains(platform));
            assertNotNull(inputSystem.state());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void testRemoveListener() {
        try (InputSystem<?> inputSystem = InputSystem.builder().discoverPlatforms().build()) {
            boolean[] called = {false};
            InputListener listener = event -> called[0] = true;
            InputEvent dummyEvent = new DefaultInputEvent(
                    null, InputEventType.KEY_PRESSED, 0, 0, 0);
            
            inputSystem.addListener(listener);
            ((InputListener)inputSystem).onInputEvent(dummyEvent);
            assertTrue(called[0]);
            
            called[0] = false;
            inputSystem.removeListener(listener);
            ((InputListener)inputSystem).onInputEvent(dummyEvent);
            assertFalse(called[0]);
        } catch (Exception e) {
            fail(e);
        }
    }
}
