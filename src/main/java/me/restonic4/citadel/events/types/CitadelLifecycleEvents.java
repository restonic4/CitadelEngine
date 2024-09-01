package me.restonic4.citadel.events.types;

import me.restonic4.citadel.core.CitadelLauncher;
import me.restonic4.citadel.core.Window;
import me.restonic4.citadel.events.Event;
import me.restonic4.citadel.events.EventFactory;

/**
 * List of events related to the lifecycle of the engine.
 */
public class CitadelLifecycleEvents {
    /**
     * Gets triggered when the engine is starting.
     */
    public static final Event<CitadelStarting> CITADEL_STARTING = EventFactory.createArray(CitadelStarting.class, callbacks -> (citadelLauncher) -> {
        for (CitadelStarting callback : callbacks) {
            callback.onCitadelStarting(citadelLauncher);
        }
    });

    @FunctionalInterface
    public interface CitadelStarting {
        void onCitadelStarting(CitadelLauncher citadelLauncher);
    }

    /**
     * Gets triggered when the engine finished the starting phase.
     */
    public static final Event<CitadelStarted> CITADEL_STARTED = EventFactory.createArray(CitadelStarted.class, callbacks -> (citadelLauncher, window) -> {
        for (CitadelStarted callback : callbacks) {
            callback.onCitadelStarted(citadelLauncher, window);
        }
    });

    @FunctionalInterface
    public interface CitadelStarted {
        void onCitadelStarted(CitadelLauncher citadelLauncher, Window window);
    }

    /**
     * Gets triggered when the engine stops.
     */
    public static final Event<CitadelStopping> CITADEL_STOPPING = EventFactory.createArray(CitadelStopping.class, callbacks -> (citadelLauncher, window) -> {
        for (CitadelStopping callback : callbacks) {
            callback.onCitadelStopping(citadelLauncher, window);
        }
    });

    @FunctionalInterface
    public interface CitadelStopping {
        void onCitadelStopping(CitadelLauncher citadelLauncher, Window window);
    }

    /**
     * Gets triggered when the engine stops.
     */
    public static final Event<CitadelStopped> CITADEL_STOPPED = EventFactory.createArray(CitadelStopped.class, callbacks -> (citadelLauncher) -> {
        for (CitadelStopped callback : callbacks) {
            callback.onCitadelStopped(citadelLauncher);
        }
    });

    @FunctionalInterface
    public interface CitadelStopped {
        void onCitadelStopped(CitadelLauncher citadelLauncher);
    }

    /**
     * Gets triggered when the engine starts cleaning up. Gets called after CitadelStopping
     */
    public static final Event<CitadelCleaningUp> CITADEL_CLEANING_UP = EventFactory.createArray(CitadelCleaningUp.class, callbacks -> (citadelLauncher, window) -> {
        for (CitadelCleaningUp callback : callbacks) {
            callback.onCitadelCleaningUp(citadelLauncher, window);
        }
    });

    @FunctionalInterface
    public interface CitadelCleaningUp {
        void onCitadelCleaningUp(CitadelLauncher citadelLauncher, Window window);
    }

    /**
     * Gets triggered when the engine finishes cleaning up.
     */
    public static final Event<CitadelCleanedUp> CITADEL_CLEANED_UP = EventFactory.createArray(CitadelCleanedUp.class, callbacks -> (citadelLauncher, window) -> {
        for (CitadelCleanedUp callback : callbacks) {
            callback.onCitadelCleanedUp(citadelLauncher, window);
        }
    });

    @FunctionalInterface
    public interface CitadelCleanedUp {
        void onCitadelCleanedUp(CitadelLauncher citadelLauncher, Window window);
    }
}
