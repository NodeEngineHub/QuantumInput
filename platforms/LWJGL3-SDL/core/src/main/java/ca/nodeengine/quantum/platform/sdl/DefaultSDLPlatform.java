package ca.nodeengine.quantum.platform.sdl;

import ca.nodeengine.quantum.api.InputDevice;
import ca.nodeengine.quantum.api.event.InputEventType;
import ca.nodeengine.quantum.api.event.InputListener;
import ca.nodeengine.quantum.api.platform.sdl.SDLPlatform;
import ca.nodeengine.quantum.event.DefaultInputEvent;
import org.jspecify.annotations.Nullable;
import org.lwjgl.sdl.SDLEvents;
import org.lwjgl.sdl.SDLInit;
import org.lwjgl.sdl.SDLError;
import org.lwjgl.sdl.SDL_KeyboardEvent;
import org.lwjgl.sdl.SDL_MouseButtonEvent;
import org.lwjgl.sdl.SDL_MouseMotionEvent;
import org.lwjgl.sdl.SDL_MouseWheelEvent;
import org.lwjgl.sdl.SDL_Event;

import java.util.*;

/**
 * Adds support for SDL through LWJGL3
 *
 * @author FX
 */
public class DefaultSDLPlatform implements SDLPlatform {

    private final Set<Integer> windows = new HashSet<>();
    private @Nullable InputListener listener;
    private final SDL_Event event = SDL_Event.create();

    @Override
    public void initialize(InputListener listener) {
        this.listener = listener;
        if (!SDLInit.SDL_Init(SDLInit.SDL_INIT_VIDEO | SDLInit.SDL_INIT_EVENTS)) {
            throw new IllegalStateException("Unable to initialize SDL: " + SDLError.SDL_GetError());
        }
    }

    @Override
    public void registerWindow(long windowHandle) {
        windows.add((int) windowHandle);
    }

    @Override
    public void unregisterWindow(long windowHandle) {
        windows.remove((int) windowHandle);
    }

    @Override
    public void update() {
        while (SDLEvents.SDL_PollEvent(event)) {
            if (listener == null) {
                continue;
            }

            switch (event.type()) {
                case SDLEvents.SDL_EVENT_KEY_DOWN:
                case SDLEvents.SDL_EVENT_KEY_UP:
                    SDL_KeyboardEvent keyEvent = event.key();
                    if (windows.contains(keyEvent.windowID())) {
                        InputEventType type = (event.type() == SDLEvents.SDL_EVENT_KEY_DOWN)
                                ? InputEventType.KEY_PRESSED
                                : InputEventType.KEY_RELEASED;
                        listener.onInputEvent(new DefaultInputEvent(
                                SDL_DEVICE, type, keyEvent.key(), 0, 0
                        ));
                    }
                    break;

                case SDLEvents.SDL_EVENT_MOUSE_BUTTON_DOWN:
                case SDLEvents.SDL_EVENT_MOUSE_BUTTON_UP:
                    SDL_MouseButtonEvent buttonEvent = event.button();
                    if (windows.contains(buttonEvent.windowID())) {
                        InputEventType type = (event.type() == SDLEvents.SDL_EVENT_MOUSE_BUTTON_DOWN)
                                ? InputEventType.BUTTON_PRESSED
                                : InputEventType.BUTTON_RELEASED;
                        listener.onInputEvent(new DefaultInputEvent(
                                SDL_DEVICE, type, buttonEvent.button(), 0, 0
                        ));
                    }
                    break;

                case SDLEvents.SDL_EVENT_MOUSE_MOTION:
                    SDL_MouseMotionEvent motionEvent = event.motion();
                    if (windows.contains(motionEvent.windowID())) {
                        listener.onInputEvent(new DefaultInputEvent(
                                SDL_DEVICE, InputEventType.MOUSE_CHANGED, 0, motionEvent.x(), motionEvent.y()
                        ));
                    }
                    break;

                case SDLEvents.SDL_EVENT_MOUSE_WHEEL:
                    SDL_MouseWheelEvent wheelEvent = event.wheel();
                    if (windows.contains(wheelEvent.windowID())) {
                        listener.onInputEvent(new DefaultInputEvent(
                                SDL_DEVICE, InputEventType.SCROLL, 0, wheelEvent.x(), wheelEvent.y()
                        ));
                    }
                    break;
            }
        }
    }

    @Override
    public void close() {
        windows.clear();
        // Note: SDL_Quit() is not called here, it should be handled by the application.
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public Collection<InputDevice> getInputDevices() {
        return List.of(SDL_DEVICE);
    }
}
