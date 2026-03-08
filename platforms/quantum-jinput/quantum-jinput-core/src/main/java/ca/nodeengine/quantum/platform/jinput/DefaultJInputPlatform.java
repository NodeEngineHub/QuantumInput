package ca.nodeengine.quantum.platform.jinput;

import ca.nodeengine.quantum.InputDevice;
import ca.nodeengine.quantum.event.InputEventType;
import ca.nodeengine.quantum.event.InputListener;
import ca.nodeengine.quantum.event.DefaultInputEvent;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link JInputPlatform}
 *
 * @author FX
 */
public class DefaultJInputPlatform implements JInputPlatform {

    private @Nullable InputListener listener;
    private Controller[] controllers = new Controller[0];
    private final Map<Controller, InputDevice> deviceMap = new HashMap<>();

    @Override
    public void initialize(InputListener listener) {
        this.listener = listener;
        try {
            this.controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
            for (Controller controller : controllers) {
                deviceMap.put(controller, new JInputDevice(controller));
            }
        } catch (Exception e) {
            // Log or handle? For now, we'll just have no controllers.
            this.controllers = new Controller[0];
        }
    }

    @Override
    public void update() {
        if (listener == null) {
            return;
        }

        for (Controller controller : controllers) {
            if (controller.poll()) {
                EventQueue queue = controller.getEventQueue();
                Event event = new Event();
                while (queue.getNextEvent(event)) {
                    processEvent(controller, event);
                }
            }
        }
    }

    private void processEvent(Controller controller, Event event) {
        if (listener == null) return;

        Component component = event.getComponent();
        Component.Identifier id = component.getIdentifier();
        float value = event.getValue();
        InputDevice device = deviceMap.get(controller);
        if (device == null) {
            return;
        }

        if (id instanceof Component.Identifier.Key) {
            InputEventType type = (value > 0.5f) ? InputEventType.KEY_PRESSED : InputEventType.KEY_RELEASED;
            listener.onInputEvent(new DefaultInputEvent(device, type, getIdentifierCode(id), value, 0));
        } else if (id instanceof Component.Identifier.Button) {
            InputEventType type = (value > 0.5f) ? InputEventType.BUTTON_PRESSED : InputEventType.BUTTON_RELEASED;
            listener.onInputEvent(new DefaultInputEvent(device, type, getIdentifierCode(id), value, 0));
        } else if (id instanceof Component.Identifier.Axis) {
            if (id == Component.Identifier.Axis.Z && controller.getType() == Controller.Type.MOUSE) {
                listener.onInputEvent(new DefaultInputEvent(device, InputEventType.SCROLL, 0, 0, value));
            } else {
                listener.onInputEvent(new DefaultInputEvent(device, InputEventType.AXIS_CHANGED,
                        getIdentifierCode(id), value, 0));
            }
        }
    }

    private int getIdentifierCode(Component.Identifier id) {
        // Use name hashCode for a relatively stable integer code.
        return id.getName().hashCode();
    }

    @Override
    public boolean isSupported() {
        try {
            // Try to access JInput to see if natives are present
            return ControllerEnvironment.getDefaultEnvironment().getControllers() != null;
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public Collection<InputDevice> getInputDevices() {
        return deviceMap.values();
    }

    @Override
    public void close() {
        deviceMap.clear();
        controllers = new Controller[0];
        listener = null;
    }

    private record JInputDevice(Controller controller) implements InputDevice {
        @Override
        public String name() {
            return controller.getName();
        }

        @Override
        public boolean isGlobal() {
            return false;
        }
    }
}
