package ca.nodeengine.quantum.platform.web;

import ca.nodeengine.quantum.InputDevice;
import ca.nodeengine.quantum.platform.QuantumPlatform;
import org.graalvm.webimage.api.JSObject;

/**
 * Adds support for browser (DOM) input through GraalVM Web Image.
 * <p>
 * Like the GLFW platform, this is a single global device. Keyboard events are captured on the browser
 * {@code window}; pointer and wheel events are captured on the registered {@code <canvas>} so their
 * coordinates are relative to it. Keyboard codes are normalized to GLFW keycodes and mouse buttons to
 * GLFW button numbering, so an application sees identical input codes on the desktop and web backends.
 *
 * @author FX
 */
public interface WebPlatform extends QuantumPlatform {

    /** The single global browser input device. */
    InputDevice WEB_DEVICE = new InputDevice() {
        @Override
        public String name() {
            return "Web";
        }

        @Override
        public boolean isGlobal() {
            return true;
        }
    };

    /**
     * Registers a canvas element to capture input from.
     * <p>
     * Pointer/wheel listeners are attached to the canvas and keyboard listeners to the window. DOM events
     * are deferred and dispatched during {@link #update()} so they land at a consistent frame boundary.
     *
     * @param canvas The canvas element to capture input on.
     */
    void registerCanvas(JSObject canvas);

    /**
     * Unregisters a previously registered canvas, removing its listeners.
     *
     * @param canvas The canvas element to stop capturing input on.
     */
    void unregisterCanvas(JSObject canvas);

    @Override
    default Class<?> getApiClass() {
        return WebPlatform.class;
    }

    @Override
    default boolean usesGlobalDevice() {
        return true;
    }
}
