package ca.nodeengine.quantum;

import ca.nodeengine.quantum.platform.QuantumPlatform;
import ca.nodeengine.quantum.state.GlobalInputState;
import ca.nodeengine.quantum.state.InputState;
import ca.nodeengine.quantum.state.PerDeviceInputState;
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

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public InputSystem<GlobalInputState> buildGlobal() {
        if (platforms.isEmpty()) {
            discoverPlatforms();
        }

        InputProcessor finalProcessor;
        if (this.processor == null) {
            finalProcessor = new DefaultInputProcessor(new DefaultGlobalInputState());
        } else {
            InputState state = this.processor.state();
            if (state instanceof GlobalInputState) {
                finalProcessor = this.processor;
            } else {
                finalProcessor = new DefaultInputProcessor(
                        new PerDeviceToGlobalInputStateAdapter((PerDeviceInputState) state, platforms.values())
                );
            }
        }

        return (InputSystem<GlobalInputState>) new DefaultInputSystem(platforms, finalProcessor);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public InputSystem<PerDeviceInputState> buildPerDevice() {
        if (platforms.isEmpty()) {
            discoverPlatforms();
        }

        InputProcessor finalProcessor;
        if (this.processor == null) {
            finalProcessor = new DefaultInputProcessor(new DefaultPerDeviceInputState());
        } else {
            InputState state = this.processor.state();
            if (state instanceof PerDeviceInputState) {
                finalProcessor = this.processor;
            } else {
                finalProcessor = new DefaultInputProcessor(
                        new GlobalToPerDeviceInputStateAdapter((GlobalInputState) state)
                );
            }
        }

        return (InputSystem<PerDeviceInputState>) new DefaultInputSystem(platforms, finalProcessor);
    }
}
