package me.restonic4.citadel.events.types;

import me.restonic4.citadel.events.Event;
import me.restonic4.citadel.events.EventFactory;
import me.restonic4.citadel.events.EventResult;
import me.restonic4.citadel.registries.AbstractRegistryInitializer;
import me.restonic4.citadel.registries.Registry;
import me.restonic4.citadel.registries.RegistryManager;

/**
 * List of events related to the {@link Registry} system.
 */
public class RegistriesEvents {
    /**
     * Gets triggered when the {@link RegistryManager} starts registering a registry set.
     */
    public static final Event<RegisteringSet> REGISTERING_SET = EventFactory.createArray(RegisteringSet.class, callbacks -> (abstractRegistryInitializer, isBuiltIn) -> {
        for (RegisteringSet callback : callbacks) {
            EventResult result = callback.onRegisteringSet(abstractRegistryInitializer, isBuiltIn);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface RegisteringSet {
        EventResult onRegisteringSet(AbstractRegistryInitializer abstractRegistryInitializer, boolean isBuiltIn);
    }

    /**
     * Gets triggered when the {@link RegistryManager} finishes registering a registry set.
     */
    public static final Event<SetRegistered> SET_REGISTERED = EventFactory.createArray(SetRegistered.class, callbacks -> (abstractRegistryInitializer, isBuiltIn) -> {
        for (SetRegistered callback : callbacks) {
            callback.onSetRegistered(abstractRegistryInitializer, isBuiltIn);
        }
    });

    @FunctionalInterface
    public interface SetRegistered {
        void onSetRegistered(AbstractRegistryInitializer abstractRegistryInitializer, boolean isBuiltIn);
    }
}
