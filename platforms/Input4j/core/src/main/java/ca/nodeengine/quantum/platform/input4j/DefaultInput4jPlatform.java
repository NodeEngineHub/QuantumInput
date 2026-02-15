package ca.nodeengine.quantum.platform.input4j;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.event.InputEventType;
import ca.nodeengine.quantum.api.event.InputListener;
import ca.nodeengine.quantum.api.platform.input4j.Input4jPlatform;
import ca.nodeengine.quantum.event.DefaultInputEvent;
import de.gurkenlabs.input4j.InputDevicePlugin;
import de.gurkenlabs.input4j.InputDevices;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link Input4jPlatform}
 *
 * @author FX
 */
public class DefaultInput4jPlatform implements Input4jPlatform {

    private @Nullable InputListener listener;
    private @Nullable InputDevicePlugin plugin;
    private final Map<de.gurkenlabs.input4j.InputDevice, InputDevice> deviceMap = new HashMap<>();

    @Override
    public void initialize(InputListener listener) {
        this.listener = listener;
        this.plugin = InputDevices.init();

        if (this.plugin != null) {
            for (de.gurkenlabs.input4j.InputDevice device : this.plugin.getAll()) {
                Input4jDevice quantumDevice = new Input4jDevice(device);
                deviceMap.put(device, quantumDevice);

                device.onInputValueChanged(e -> {
                    if (this.listener == null) {
                        return;
                    }

                    float newValue = e.newValue();
                    // Determine event type based on value for now.
                    // Buttons are typically 0.0 or 1.0.
                    InputEventType type = (newValue == 0F || newValue == 1F)
                            ? (newValue == 1F ? InputEventType.BUTTON_PRESSED : InputEventType.BUTTON_RELEASED)
                            : InputEventType.AXIS_CHANGED;

                    this.listener.onInputEvent(new DefaultInputEvent(
                            quantumDevice,
                            type,
                            e.component().hashCode(),
                            newValue,
                            0
                    ));
                });
            }
        }
    }

    @Override
    public void update() {
        if (this.plugin == null) {
            return;
        }

        for (de.gurkenlabs.input4j.InputDevice device : this.plugin.getAll()) {
            device.poll();
        }
    }

    @Override
    public void close() {
        if (plugin != null) {
            try {
                plugin.close();
            } catch (IOException e) {
                // Ignore
            }
            plugin = null;
        }
        deviceMap.clear();
        listener = null;
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public Collection<InputDevice> getInputDevices() {
        return deviceMap.values();
    }


    private record Input4jDevice(de.gurkenlabs.input4j.InputDevice device) implements InputDevice {
        @Override
        public String name() {
            return device.getName();
        }

        @Override
        public boolean isGlobal() {
            return false;
        }
    }
}
