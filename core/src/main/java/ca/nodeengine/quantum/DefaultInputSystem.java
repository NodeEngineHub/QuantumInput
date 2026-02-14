package ca.nodeengine.quantum;

import ca.nodeengine.quantum.api.InputProcessor;
import ca.nodeengine.quantum.api.InputSystem;
import ca.nodeengine.quantum.api.event.InputEvent;
import ca.nodeengine.quantum.api.event.InputListener;
import ca.nodeengine.quantum.api.state.InputState;
import ca.nodeengine.quantum.api.platform.QuantumPlatform;
import ca.nodeengine.quantum.state.MutableInputState;
import org.jspecify.annotations.Nullable;

import java.util.*;

/**
 * The default implementation of {@link InputSystem}<br>
 * This handles the creator of {@link QuantumPlatform}'s through the service loader.
 *
 * @param <IS> The input state type
 * @author FX
 */
public class DefaultInputSystem<IS extends InputState & MutableInputState> implements InputSystem<IS>, InputListener {

    private final Map<Class<?>, QuantumPlatform> platforms;
    private final InputProcessor<IS> processor;
    private final List<InputListener> listeners = new ArrayList<>();

    public DefaultInputSystem() {
        this.platforms = loadPlatformsFromServiceLoader();
        IS inputState = createInputState(); // Must be done after platforms are loaded.
        this.processor = new DefaultInputProcessor<>(inputState);
        for (QuantumPlatform platform : platforms.values()) {
            platform.initialize(this);
        }
    }

    public DefaultInputSystem(IS inputState) {
        this(new DefaultInputProcessor<>(inputState));
    }

    public DefaultInputSystem(InputProcessor<IS> processor) {
        this.platforms = loadPlatformsFromServiceLoader();
        this.processor = processor;
        for (QuantumPlatform platform : platforms.values()) {
            platform.initialize(this);
        }
    }

    private IS createInputState() {
        boolean global = true;
        for (QuantumPlatform platform : this.platforms.values()) {
            if (!platform.usesGlobalDevice()) {
                global = false;
                break;
            }
        }
        if (global) {
            //noinspection unchecked
            return (IS) new DefaultGlobalInputState();
        }
        //noinspection unchecked
        return (IS) new DefaultPerDeviceInputState();
    }

    @Override
    public void update() {
        for (QuantumPlatform platform : platforms.values()) {
            platform.update();
        }
    }

    @Override
    public IS state() {
        return processor.state();
    }

    @Override
    public @Nullable <P extends QuantumPlatform> P getPlatform(Class<P> apiClass) {
        //noinspection unchecked
        return (P) platforms.get(apiClass);
    }

    @Override
    public Collection<QuantumPlatform> getPlatforms() {
        return platforms.values();
    }

    @Override
    public void addListener(InputListener listener) {
        listeners.add(listener);
    }

    @Override
    public void close() throws Exception {
        for (QuantumPlatform platform : platforms.values()) {
            platform.close();
        }
    }

    @Override
    public void onInputEvent(InputEvent event) {
        this.processor.onInputEvent(event);
        this.listeners.forEach(t -> t.onInputEvent(event));
    }

    private static Map<Class<?>, QuantumPlatform> loadPlatformsFromServiceLoader() {
        Map<Class<?>, QuantumPlatform> platforms = new HashMap<>();
        ServiceLoader.load(QuantumPlatform.class).forEach(platform -> {
            if (platform.isSupported()) {
                platforms.put(platform.getApiClass(), platform);
            }
        });
        return platforms;
    }
}
