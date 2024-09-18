package com.restonic4.citadel.events.types;

import com.restonic4.citadel.events.Event;
import com.restonic4.citadel.events.EventFactory;
import com.restonic4.citadel.events.EventResult;
import com.restonic4.citadel.util.debug.DebugManager;

/**
 * List of events related to debug stuff.
 */
public class DebugEvents {
    /**
     * Gets triggered when the {@link DebugManager} changes it's mode.
     */
    public static final Event<DebugModeChanged> DEBUG_MODE_CHANGED = EventFactory.createArray(DebugModeChanged.class, callbacks -> (oldValue, newValue) -> {
        for (DebugModeChanged callback : callbacks) {
            EventResult result = callback.onDebugModeChanged(oldValue, newValue);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface DebugModeChanged {
        EventResult onDebugModeChanged(boolean oldValue, boolean newValue);
    }
}
