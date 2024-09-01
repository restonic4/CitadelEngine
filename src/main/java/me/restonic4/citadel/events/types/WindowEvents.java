package me.restonic4.citadel.events.types;

import me.restonic4.citadel.core.CitadelLauncher;
import me.restonic4.citadel.core.Window;
import me.restonic4.citadel.events.Event;
import me.restonic4.citadel.events.EventFactory;
import me.restonic4.citadel.events.EventResult;

public class WindowEvents {
    /**
     * Gets triggered when the window gets created.
     */
    public static final Event<WindowCreated> CREATED = EventFactory.createArray(WindowCreated.class, callbacks -> (window) -> {
        for (WindowCreated callback : callbacks) {
            callback.onWindowCreated(window);
        }
    });

    @FunctionalInterface
    public interface WindowCreated {
        void onWindowCreated(Window window);
    }

    /**
     * Gets triggered when the window gets closed.
     */
    public static final Event<WindowClosed> CLOSED = EventFactory.createArray(WindowClosed.class, callbacks -> (window) -> {
        for (WindowClosed callback : callbacks) {
            callback.onWindowClosed(window);
        }
    });

    @FunctionalInterface
    public interface WindowClosed {
        void onWindowClosed(Window window);
    }

    /**
     * Gets triggered when the window gets resized.
     */
    public static final Event<WindowResized> RESIZED = EventFactory.createArray(WindowResized.class, callbacks -> (window, newWidth, newHeight) -> {
        for (WindowResized callback : callbacks) {
            EventResult result = callback.onWindowResized(window, newWidth, newHeight);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface WindowResized {
        EventResult onWindowResized(Window window, int newWidth, int newHeight);
    }
}
