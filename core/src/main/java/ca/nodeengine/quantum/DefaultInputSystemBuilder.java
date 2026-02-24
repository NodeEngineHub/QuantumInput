package ca.nodeengine.quantum;

import ca.nodeengine.quantum.api.InputProcessor;
import ca.nodeengine.quantum.api.InputSystem;
import ca.nodeengine.quantum.api.platform.QuantumPlatform;
import ca.nodeengine.quantum.api.state.InputState;
import ca.nodeengine.quantum.api.state.GlobalInputState;
import ca.nodeengine.quantum.api.state.PerDeviceInputState;
import ca.nodeengine.quantum.api.exception.QuantumInputException;
import ca.nodeengine.quantum.state.MutableInputState;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Default implementation of {@link InputSystem.Builder}.
 *
 * @param <IS> The type of the input state.
 * @author FX
 */
public class DefaultInputSystemBuilder<IS extends InputState> implements InputSystem.Builder<IS> {

    private final Map<Class<?>, QuantumPlatform> platforms = new HashMap<>();
    private @Nullable InputProcessor<IS> processor;

    @Override
    public InputSystem.Builder<IS> withPlatform(QuantumPlatform platform) {
        platforms.put(platform.getApiClass(), platform);
        return this;
    }

    @Override
    public InputSystem.Builder<IS> discoverPlatforms() {
        ServiceLoader.load(QuantumPlatform.class).forEach(platform -> {
            if (platform.isSupported()) {
                platforms.put(platform.getApiClass(), platform);
            }
        });
        return this;
    }

    @Override
    public InputSystem.Builder<IS> withInputProcessor(InputProcessor<IS> processor) {
        this.processor = processor;
        return this;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public InputSystem<IS> build() {
        if (platforms.isEmpty()) {
            discoverPlatforms();
        }

        InputProcessor<IS> finalProcessor = this.processor;
        if (finalProcessor == null) {
            boolean global = true;
            for (QuantumPlatform platform : platforms.values()) {
                if (!platform.usesGlobalDevice()) {
                    global = false;
                    break;
                }
            }

            finalProcessor = (InputProcessor<IS>) new DefaultInputProcessor(
                    global ? new DefaultGlobalInputState() : new DefaultPerDeviceInputState()
            );
        }

        return (InputSystem<IS>) new DefaultInputSystem(platforms, finalProcessor);
    }
}
