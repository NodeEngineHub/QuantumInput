package ca.nodeengine.quantum.platform.javafx;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.event.InputEventType;
import ca.nodeengine.quantum.api.event.InputListener;
import ca.nodeengine.quantum.api.platform.javafx.JavaFXPlatform;
import ca.nodeengine.quantum.event.DefaultInputEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adds support for JavaFX
 *
 * @author FX
 */
public class DefaultJavaFXPlatform implements JavaFXPlatform {

    private final Map<Object, JavaFXListeners> scenes = new HashMap<>();
    private @Nullable InputListener listener;

    @Override
    public void initialize(InputListener listener) {
        this.listener = listener;
    }

    @Override
    public void registerScene(Object scene) {
        if (scene instanceof Scene fxScene) {
            if (!scenes.containsKey(fxScene)) {
                JavaFXListeners listeners = new JavaFXListeners();
                scenes.put(fxScene, listeners);
                fxScene.addEventHandler(KeyEvent.KEY_PRESSED, listeners.keyPressedHandler);
                fxScene.addEventHandler(KeyEvent.KEY_RELEASED, listeners.keyReleasedHandler);
                fxScene.addEventHandler(MouseEvent.MOUSE_PRESSED, listeners.mousePressedHandler);
                fxScene.addEventHandler(MouseEvent.MOUSE_RELEASED, listeners.mouseReleasedHandler);
                fxScene.addEventHandler(MouseEvent.MOUSE_MOVED, listeners.mouseMovedHandler);
                fxScene.addEventHandler(MouseEvent.MOUSE_DRAGGED, listeners.mouseDraggedHandler);
                fxScene.addEventHandler(ScrollEvent.SCROLL, listeners.scrollHandler);
            }
        }
    }

    @Override
    public void unregisterScene(Object scene) {
        JavaFXListeners listeners = scenes.remove(scene);
        if (listeners != null && scene instanceof Scene fxScene) {
            fxScene.removeEventHandler(KeyEvent.KEY_PRESSED, listeners.keyPressedHandler);
            fxScene.removeEventHandler(KeyEvent.KEY_RELEASED, listeners.keyReleasedHandler);
            fxScene.removeEventHandler(MouseEvent.MOUSE_PRESSED, listeners.mousePressedHandler);
            fxScene.removeEventHandler(MouseEvent.MOUSE_RELEASED, listeners.mouseReleasedHandler);
            fxScene.removeEventHandler(MouseEvent.MOUSE_MOVED, listeners.mouseMovedHandler);
            fxScene.removeEventHandler(MouseEvent.MOUSE_DRAGGED, listeners.mouseDraggedHandler);
            fxScene.removeEventHandler(ScrollEvent.SCROLL, listeners.scrollHandler);
        }
    }

    @Override
    public void update() {
        // JavaFX handles events in its own Application Thread
    }

    @Override
    public void close() {
        for (Object scene : scenes.keySet().toArray()) {
            unregisterScene(scene);
        }
    }

    @Override
    public boolean isSupported() {
        try {
            Class.forName("javafx.scene.Scene");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public Collection<InputDevice> getInputDevices() {
        return List.of(JAVAFX_DEVICE);
    }


    private class JavaFXListeners {
        final EventHandler<KeyEvent> keyPressedHandler;
        final EventHandler<KeyEvent> keyReleasedHandler;
        final EventHandler<MouseEvent> mousePressedHandler;
        final EventHandler<MouseEvent> mouseReleasedHandler;
        final EventHandler<MouseEvent> mouseMovedHandler;
        final EventHandler<MouseEvent> mouseDraggedHandler;
        final EventHandler<ScrollEvent> scrollHandler;

        JavaFXListeners() {
            this.keyPressedHandler = e -> {
                if (listener != null) {
                    listener.onInputEvent(new DefaultInputEvent(
                            JAVAFX_DEVICE, InputEventType.KEY_PRESSED, e.getCode().getCode(), 0, 0
                    ));
                }
            };

            this.keyReleasedHandler = e -> {
                if (listener != null) {
                    listener.onInputEvent(new DefaultInputEvent(
                            JAVAFX_DEVICE, InputEventType.KEY_RELEASED, e.getCode().getCode(), 0, 0
                    ));
                }
            };

            this.mousePressedHandler = e -> {
                if (listener != null) {
                    int button = switch (e.getButton()) {
                        case PRIMARY -> 1;
                        case MIDDLE -> 2;
                        case SECONDARY -> 3;
                        default -> 0;
                    };
                    listener.onInputEvent(new DefaultInputEvent(
                            JAVAFX_DEVICE, InputEventType.BUTTON_PRESSED, button, 0, 0
                    ));
                }
            };

            this.mouseReleasedHandler = e -> {
                if (listener != null) {
                    int button = switch (e.getButton()) {
                        case PRIMARY -> 1;
                        case MIDDLE -> 2;
                        case SECONDARY -> 3;
                        default -> 0;
                    };
                    listener.onInputEvent(new DefaultInputEvent(
                            JAVAFX_DEVICE, InputEventType.BUTTON_RELEASED, button, 0, 0
                    ));
                }
            };

            this.mouseMovedHandler = e -> {
                if (listener != null) {
                    listener.onInputEvent(new DefaultInputEvent(
                            JAVAFX_DEVICE, InputEventType.MOUSE_CHANGED, 0, (float) e.getX(), (float) e.getY()
                    ));
                }
            };

            this.mouseDraggedHandler = e -> {
                if (listener != null) {
                    listener.onInputEvent(new DefaultInputEvent(
                            JAVAFX_DEVICE, InputEventType.MOUSE_CHANGED, 0, (float) e.getX(), (float) e.getY()
                    ));
                }
            };

            this.scrollHandler = e -> {
                if (listener != null) {
                    listener.onInputEvent(new DefaultInputEvent(
                            JAVAFX_DEVICE, InputEventType.SCROLL, 0, (float) e.getDeltaX(), (float) e.getDeltaY()
                    ));
                }
            };
        }
    }
}
