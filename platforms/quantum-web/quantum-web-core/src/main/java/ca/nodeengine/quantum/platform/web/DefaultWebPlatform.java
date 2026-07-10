package ca.nodeengine.quantum.platform.web;

import ca.nodeengine.quantum.InputDevice;
import ca.nodeengine.quantum.Keys;
import ca.nodeengine.quantum.MouseButtons;
import ca.nodeengine.quantum.Settings;
import ca.nodeengine.quantum.event.DefaultInputEvent;
import ca.nodeengine.quantum.event.InputEvent;
import ca.nodeengine.quantum.event.InputEventType;
import ca.nodeengine.quantum.event.InputListener;
import org.graalvm.webimage.api.JSObject;
import org.jspecify.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * The GraalVM Web Image {@link WebPlatform} implementation.
 * <p>
 * DOM input events fire asynchronously between frames. To match the frame-coherent contract of the
 * other platforms (events are applied at a single, consistent point in the frame), every DOM listener
 * here <b>enqueues</b> a ready {@link InputEvent} rather than dispatching immediately. {@link #update()}
 * called once per frame by the input system, drains that queue and forwards each event to the
 * {@link InputListener}, so both the polled state and any listeners change at one point in the frame.
 * <p>
 * Coordinates are reported in <b>framebuffer (canvas backing-store) pixels</b> so input lines up with the
 * render viewport: {@code (clientX - rectLeft) * devicePixelRatio}.
 *
 * @author FX
 */
public class DefaultWebPlatform implements WebPlatform {

    private final Queue<InputEvent> eventQueue = new ArrayDeque<>();
    private final List<CanvasRegistration> canvases = new ArrayList<>();

    private @Nullable InputListener listener;

    // Keyboard is captured at the window level (canvases are not focusable by default). Retained so the
    // exact same references can be removed in close().
    private @Nullable JSObject windowTarget;
    private @Nullable Consumer<JSObject> keyDownListener;
    private @Nullable Consumer<JSObject> keyUpListener;

    @Override
    public void initialize(InputListener listener) {
        this.listener = listener;
        // Keyboard events target the global window; attach once for the lifetime of the platform.
        JSObject window = WebInterop.window();
        this.windowTarget = window;
        this.keyDownListener = this::onDomKeyDown;
        this.keyUpListener = this::onDomKeyUp;
        WebInterop.addEventListener(window, "keydown", this.keyDownListener);
        WebInterop.addEventListener(window, "keyup", this.keyUpListener);
    }

    @Override
    public void registerCanvas(JSObject canvas) {
        for (CanvasRegistration reg : this.canvases) {
            if (reg.canvas == canvas) {
                return; // Already registered.
            }
        }
        CanvasRegistration reg = new CanvasRegistration(canvas);
        WebInterop.addEventListener(canvas, "pointerdown", reg.pointerDown);
        WebInterop.addEventListener(canvas, "pointerup", reg.pointerUp);
        WebInterop.addEventListener(canvas, "pointermove", reg.pointerMove);
        WebInterop.addEventListener(canvas, "wheel", reg.wheel);
        // Suppress the right-click context menu so RIGHT button drags work.
        WebInterop.addEventListener(canvas, "contextmenu", reg.contextMenu);
        this.canvases.add(reg);
    }

    @Override
    public void unregisterCanvas(JSObject canvas) {
        CanvasRegistration found = null;
        for (CanvasRegistration reg : this.canvases) {
            if (reg.canvas == canvas) {
                found = reg;
                break;
            }
        }
        if (found != null) {
            removeCanvasListeners(found);
            this.canvases.remove(found);
        }
    }

    @Override
    public void update() {
        // Drain a snapshot count so events enqueued during the flush (re-entrant, rare) defer to the
        // next frame and termination is guaranteed.
        InputListener target = this.listener;
        int pending = this.eventQueue.size();
        for (int i = 0; i < pending; i++) {
            InputEvent event = this.eventQueue.poll();
            if (event == null) {
                break;
            }
            if (target != null) {
                target.onInputEvent(event);
            }
        }
    }

    @Override
    public void close() {
        if (this.windowTarget != null) {
            if (this.keyDownListener != null) {
                WebInterop.removeEventListener(this.windowTarget, "keydown", this.keyDownListener);
            }
            if (this.keyUpListener != null) {
                WebInterop.removeEventListener(this.windowTarget, "keyup", this.keyUpListener);
            }
        }
        for (CanvasRegistration reg : this.canvases) {
            removeCanvasListeners(reg);
        }
        this.canvases.clear();
        this.eventQueue.clear();
    }

    @Override
    public boolean isSupported() {
        try {
            return WebInterop.hasBrowser();
        } catch (Throwable t) {
            // Not running on GraalVM Web Image (the @JS native has no JVM implementation).
            return false;
        }
    }

    @Override
    public Collection<InputDevice> getInputDevices() {
        return List.of(WEB_DEVICE);
    }

    private void removeCanvasListeners(CanvasRegistration reg) {
        WebInterop.removeEventListener(reg.canvas, "pointerdown", reg.pointerDown);
        WebInterop.removeEventListener(reg.canvas, "pointerup", reg.pointerUp);
        WebInterop.removeEventListener(reg.canvas, "pointermove", reg.pointerMove);
        WebInterop.removeEventListener(reg.canvas, "wheel", reg.wheel);
        WebInterop.removeEventListener(reg.canvas, "contextmenu", reg.contextMenu);
    }

    // --- Coordinate + button helpers --------------------------------------------------------------

    private static float canvasX(JSObject event, JSObject canvas) {
        double clientX = WebInterop.eventClientX(event);
        double left = WebInterop.boundingRect(canvas, "left");
        return (float) Math.round((clientX - left) * WebInterop.devicePixelRatio());
    }

    private static float canvasY(JSObject event, JSObject canvas) {
        double clientY = WebInterop.eventClientY(event);
        double top = WebInterop.boundingRect(canvas, "top");
        return (float) Math.round((clientY - top) * WebInterop.devicePixelRatio());
    }

    /**
     * Maps a DOM {@code MouseEvent.button} (0 = left, 1 = middle, 2 = right, 3 = back*, 4 = forward*) to
     * QuantumInput button numbering.<br>
     * * Typical binding, but can be anything.
     *
     * @param domButton The DOM button number.
     * @return The QuantumInput button number.
     */
    private static int mapButton(int domButton) {
        if (!Settings.STANDARDIZE_MOUSE_BUTTONS) {
            return domButton;
        }
        return switch (domButton) {
            case 0 -> MouseButtons.LEFT;
            case 1 -> MouseButtons.MIDDLE;
            case 2 -> MouseButtons.RIGHT;
            default -> domButton; // 3 -> 3, 4 -> 4, etc...
        };
    }

    // --- DOM listeners (enqueue only) -------------------------------------------------------------

    private void onDomKeyDown(JSObject event) {
        // Auto-repeat keydowns are dropped: the platform emits only PRESS/RELEASE, and the key is already
        // held in the state.
        if (WebInterop.eventRepeat(event)) {
            return;
        }
        int keycode = mapKey(event);
        if (keycode != Keys.UNKNOWN) {
            enqueue(InputEventType.KEY_PRESSED, keycode, 0, 0);
        }
    }

    private void onDomKeyUp(JSObject event) {
        int keycode = mapKey(event);
        if (keycode != Keys.UNKNOWN) {
            enqueue(InputEventType.KEY_RELEASED, keycode, 0, 0);
        }
    }

    /**
     * Maps a DOM keyboard event to a canonical {@link Keys} code (translating {@code KeyboardEvent.code}
     * via {@link WebKeys}), or to the raw legacy {@code KeyboardEvent.keyCode} when key standardization is
     * disabled.
     *
     * @param event The DOM keyboard event.
     * @return The canonical (or raw) key code.
     */
    private static int mapKey(JSObject event) {
        if (!Settings.STANDARDIZE_KEYS) {
            return WebInterop.eventKeyCode(event);
        }
        return WebKeys.toQuantum(WebInterop.eventCode(event));
    }

    private void onDomPointerDown(JSObject event, JSObject canvas) {
        float x = canvasX(event, canvas);
        float y = canvasY(event, canvas);
        int button = mapButton(WebInterop.eventButton(event));
        // Seed the position before the button so a listener reading the current mouse on BUTTON_PRESSED
        // sees the click location (button events carry no coordinates).
        enqueue(InputEventType.MOUSE_CHANGED, 0, x, y);
        if (button >= 0) {
            enqueue(InputEventType.BUTTON_PRESSED, button, 0, 0);
        }
    }

    private void onDomPointerUp(JSObject event, JSObject canvas) {
        float x = canvasX(event, canvas);
        float y = canvasY(event, canvas);
        int button = mapButton(WebInterop.eventButton(event));
        enqueue(InputEventType.MOUSE_CHANGED, 0, x, y);
        if (button >= 0) {
            enqueue(InputEventType.BUTTON_RELEASED, button, 0, 0);
        }
    }

    private void onDomPointerMove(JSObject event, JSObject canvas) {
        enqueue(InputEventType.MOUSE_CHANGED, 0, canvasX(event, canvas), canvasY(event, canvas));
    }

    private void onDomWheel(JSObject event) {
        // Prevent the page from scrolling when the pointer is over the canvas.
        WebInterop.preventDefault(event);
        enqueue(InputEventType.SCROLL, 0, (float) WebInterop.eventDeltaX(event), (float) WebInterop.eventDeltaY(event));
    }

    private void enqueue(InputEventType type, int code, float value1, float value2) {
        this.eventQueue.add(new DefaultInputEvent(WEB_DEVICE, type, code, value1, value2));
    }

    /**
     * Holds the per-canvas DOM listeners so the exact same references can be removed later. Pointer and
     * wheel handlers bind the canvas so coordinate math uses the element the event fired on.
     */
    private final class CanvasRegistration {
        private final JSObject canvas;
        private final Consumer<JSObject> pointerDown;
        private final Consumer<JSObject> pointerUp;
        private final Consumer<JSObject> pointerMove;
        private final Consumer<JSObject> wheel;
        private final Consumer<JSObject> contextMenu;

        private CanvasRegistration(JSObject canvas) {
            this.canvas = canvas;
            this.pointerDown = event -> onDomPointerDown(event, canvas);
            this.pointerUp = event -> onDomPointerUp(event, canvas);
            this.pointerMove = event -> onDomPointerMove(event, canvas);
            this.wheel = DefaultWebPlatform.this::onDomWheel;
            this.contextMenu = WebInterop::preventDefault;
        }
    }
}
