package ca.nodeengine.quantum;

import ca.nodeengine.quantum.api.InputProcessor;
import ca.nodeengine.quantum.api.InputState;
import ca.nodeengine.quantum.api.InputSystem;
import ca.nodeengine.quantum.api.platform.QuantumPlatform;
import ca.nodeengine.quantum.api.platform.WindowPlatform;

import java.util.ArrayList;
import java.util.List;

public class DefaultInputSystem implements InputSystem {

    private final List<QuantumPlatform> platforms;
    private final InputProcessor processor;

    public DefaultInputSystem() {
        this(loadPlatformsFromServiceLoader());
    }

    private static List<QuantumPlatform> loadPlatformsFromServiceLoader() {
        List<QuantumPlatform> platforms = new ArrayList<>();
        QuantumPlatform.createServiceLoader().forEach(platform -> {
            if (platform.isSupported()) {
                platforms.add(platform);
            }
        });
        return platforms;
    }

    public DefaultInputSystem(List<QuantumPlatform> platforms) {
        this(platforms, new DefaultInputProcessor(new DefaultInputState()));
    }

    public DefaultInputSystem(List<QuantumPlatform> platforms, InputProcessor processor) {
        this.platforms = platforms;
        this.processor = processor;
        for (QuantumPlatform platform : platforms) {
            platform.initialize(processor);
        }
    }

    @Override
    public void poll() {
        for (QuantumPlatform platform : platforms) {
            platform.poll();
        }
    }

    @Override
    public InputState rawState() {
        return processor.state();
    }

    @Override
    public void registerWindow(long windowHandle) {
        for (QuantumPlatform platform : platforms) {
            if (platform instanceof WindowPlatform windowPlatform) {
                windowPlatform.registerWindow(windowHandle);
            }
        }
    }

    @Override
    public void unregisterWindow(long windowHandle) {
        for (QuantumPlatform platform : platforms) {
            if (platform instanceof WindowPlatform windowPlatform) {
                windowPlatform.unregisterWindow(windowHandle);
            }
        }
    }

    public void terminate() {
        for (QuantumPlatform platform : platforms) {
            platform.terminate();
        }
    }
}
