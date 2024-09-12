package me.restonic4.citadel.events.types;

import me.restonic4.ClientSide;
import me.restonic4.citadel.core.Window;
import me.restonic4.citadel.events.Event;
import me.restonic4.citadel.events.EventFactory;
import me.restonic4.citadel.events.EventResult;

/**
 * List of events related to the window.
 */
@ClientSide
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

    /**
     * Gets triggered when the window title wants to be changed.
     */
    public static final Event<WindowTitleChanging> TITLE_CHANGING = EventFactory.createArray(WindowTitleChanging.class, callbacks -> (window, oldTitle, newTitle) -> {
        for (WindowTitleChanging callback : callbacks) {
            EventResult result = callback.onWindowTitleChanging(window, oldTitle, newTitle);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface WindowTitleChanging {
        EventResult onWindowTitleChanging(Window window, String oldTitle, String newTitle);
    }
}
