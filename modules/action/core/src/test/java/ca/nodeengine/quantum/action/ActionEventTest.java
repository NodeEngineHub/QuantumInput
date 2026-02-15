package ca.nodeengine.quantum.action;

import ca.nodeengine.quantum.api.action.ActionEvent;
import ca.nodeengine.quantum.api.event.InputEvent;
import ca.nodeengine.quantum.api.event.InputEventType;
import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.event.InputListener;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Action events
 *
 * @author FX
 */
class ActionEventTest {

    @Test
    void bridgeEvents() {
        DefaultActionMap actionMap = new DefaultActionMap();
        actionMap.add(new DigitalBinding(null, "Jump", 32));

        List<ActionEvent> receivedEvents = new ArrayList<>();
        actionMap.addActionListener("base", receivedEvents::add);
        InputListener bridge = actionMap.createInputListener();

        InputEvent event = new MockInputEvent(32, InputEventType.KEY_PRESSED);
        bridge.onInputEvent(event);

        assertEquals(1, receivedEvents.size());
        assertEquals("Jump", receivedEvents.getFirst().action());
        assertEquals(1.0f, receivedEvents.getFirst().value());
        assertTrue(receivedEvents.getFirst().isActive());

        InputEvent releaseEvent = new MockInputEvent(32, InputEventType.KEY_RELEASED);
        bridge.onInputEvent(releaseEvent);

        assertEquals(2, receivedEvents.size());
        assertEquals("Jump", receivedEvents.getLast().action());
        assertEquals(0.0f, receivedEvents.getLast().value());
        assertFalse(receivedEvents.getLast().isActive());
    }

    private record MockInputEvent(int code, InputEventType type) implements InputEvent {
        @Override
        public InputDevice device() {
            return null;
        }

        @Override
        public float value1() {
            return 0;
        }

        @Override
        public float value2() {
            return 0;
        }
    }
}
