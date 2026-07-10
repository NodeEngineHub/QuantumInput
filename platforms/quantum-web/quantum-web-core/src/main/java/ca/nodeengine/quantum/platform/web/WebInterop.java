package ca.nodeengine.quantum.platform.web;

import org.graalvm.webimage.api.JS;
import org.graalvm.webimage.api.JSObject;

import java.util.function.Consumer;

/**
 * The {@code @JS} snippet methods the web platform needs from the browser.
 * <p>
 * GraalVM Web Image only supports {@code @JS} snippet methods (whose bodies are raw JavaScript that
 * replaces the native method) plus plain {@link JSObject} handles. This is the input-only subset of the
 * DOM surface (event listeners, event-field accessors, bounding-rect, device pixel ratio); it mirrors
 * the node-engine web backend's {@code WebJs} but is kept here so the platform is self-contained.
 * <p>
 * The snippets reference their parameters by name, which requires compiling with {@code -parameters}
 * (configured in this module's build).
 *
 * @author FX
 */
final class WebInterop {

    private WebInterop() {
    }

    /**
     * Reports whether a browser DOM environment is present.
     * <p>
     * On a plain JVM this native method has no implementation and throws, which the platform's
     * {@code isSupported()} treats as "unsupported".
     *
     * @return {@code true} when {@code window} and {@code document} exist.
     */
    @JS.Coerce
    @JS("return (typeof window !== 'undefined') && (typeof document !== 'undefined');")
    static native boolean hasBrowser();

    /**
     * Gets the global {@code window}, used as the keyboard event target.
     *
     * @return The {@code window} object.
     */
    @JS.Coerce
    @JS("return window;")
    static native JSObject window();

    /**
     * Gets the device pixel ratio.
     *
     * @return The device pixel ratio, defaulting to {@code 1} when unavailable.
     */
    @JS.Coerce
    @JS("return window.devicePixelRatio || 1;")
    static native double devicePixelRatio();

    /**
     * Adds an event listener to an object. The handler receives the DOM event object.
     *
     * @param target  The event target (element, window, document, ...).
     * @param event   The event name.
     * @param handler The listener; receives the event as a {@link JSObject}.
     */
    @JS.Coerce
    @JS("target.addEventListener(event, handler);")
    static native void addEventListener(JSObject target, String event, Consumer<JSObject> handler);

    /**
     * Removes a previously-added event listener. The handler reference must be the exact object passed
     * to {@link #addEventListener}.
     *
     * @param target  The event target.
     * @param event   The event name.
     * @param handler The listener that was registered.
     */
    @JS.Coerce
    @JS("target.removeEventListener(event, handler);")
    static native void removeEventListener(JSObject target, String event, Consumer<JSObject> handler);

    /**
     * Reads a bounding-client-rect field of an element.
     *
     * @param element The element.
     * @param field   One of {@code "left"}, {@code "top"}, {@code "width"}, {@code "height"}.
     * @return The requested rect value in CSS pixels.
     */
    @JS.Coerce
    @JS("return element.getBoundingClientRect()[field];")
    static native double boundingRect(JSObject element, String field);

    /**
     * Prevents the default browser action for an event (e.g. context menu, page scroll on wheel).
     *
     * @param event The event to suppress.
     */
    @JS.Coerce
    @JS("if (event && event.preventDefault) { event.preventDefault(); }")
    static native void preventDefault(JSObject event);

    // --- DOM event field accessors ----------------------------------------------------------------

    /**
     * Reads {@code event.clientX}.
     *
     * @param event A pointer/mouse event.
     * @return {@code event.clientX}.
     */
    @JS.Coerce
    @JS("return event.clientX;")
    static native double eventClientX(JSObject event);

    /**
     * Reads {@code event.clientY}.
     *
     * @param event A pointer/mouse event.
     * @return {@code event.clientY}.
     */
    @JS.Coerce
    @JS("return event.clientY;")
    static native double eventClientY(JSObject event);

    /**
     * Reads {@code event.button}.
     *
     * @param event A pointer/mouse event.
     * @return {@code event.button} (0 = primary/left, 1 = auxiliary/middle, 2 = secondary/right).
     */
    @JS.Coerce
    @JS("return event.button;")
    static native int eventButton(JSObject event);

    /**
     * Reads {@code event.deltaX}.
     *
     * @param event A wheel event.
     * @return {@code event.deltaX}.
     */
    @JS.Coerce
    @JS("return event.deltaX;")
    static native double eventDeltaX(JSObject event);

    /**
     * Reads {@code event.deltaY}.
     *
     * @param event A wheel event.
     * @return {@code event.deltaY}.
     */
    @JS.Coerce
    @JS("return event.deltaY;")
    static native double eventDeltaY(JSObject event);

    /**
     * Reads {@code event.code}.
     *
     * @param event A keyboard event.
     * @return {@code event.code} (physical key, e.g. {@code "KeyA"}, {@code "ArrowLeft"}).
     */
    @JS.Coerce
    @JS("return event.code;")
    static native String eventCode(JSObject event);

    /**
     * Reads the legacy numeric {@code event.keyCode}.
     * <p>
     * Layout-dependent and deprecated, but it is the only "raw" integer the browser offers, so it is used
     * as the non-standardized key code (when {@code quantum.standardizeKeys} is disabled).
     *
     * @param event A keyboard event.
     * @return {@code event.keyCode}.
     */
    @JS.Coerce
    @JS("return event.keyCode;")
    static native int eventKeyCode(JSObject event);

    /**
     * Reads {@code event.repeat}.
     *
     * @param event A keyboard event.
     * @return {@code true} if this is a held-key auto-repeat.
     */
    @JS.Coerce
    @JS("return event.repeat === true;")
    static native boolean eventRepeat(JSObject event);
}
