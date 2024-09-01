package me.restonic4.citadel.events;

import me.restonic4.citadel.events.types.*;

import java.lang.FunctionalInterface;
import java.util.function.Function;

/**
 * With this you can create your own events, subscribe and trigger them.
 * <p>
 * You will need to create a new class, like {@link CitadelLifecycleEvents}.
 * You need something like this to register your event:
 * <pre>{@code
 *      public static final Event<CitadelStarting> CITADEL_STARTING = EventFactory.createArray(CitadelStarting.class, callbacks -> (citadelLauncher) -> {
 *         for (CitadelStarting callback : callbacks) {
 *             callback.onCitadelStarting(citadelLauncher);
 *         }
 *     });
 *
 *     @FunctionalInterface
 *     public interface CitadelStarting {
 *         void onCitadelStarting(CitadelLauncher citadelLauncher);
 *     }}</pre>
 * <p>
 * As you can see, you need a variable to store your event by calling the {@link EventFactory#createArray(Class, Function)}}, then you will need a {@link FunctionalInterface} so you can pass it into the {@link EventFactory}
 * <p>
 * You can also take a look at {@link WindowEvents#RESIZED}, it uses {@link EventResult} to change the state of the event and potentially cancel the behaviour of the engine.
 */
public class EventFactory {
    public static <T> Event<T> createArray(Class<T> type, Function<T[], T> invokerFactory) {
        return new Event<>(invokerFactory, type);
    }
}
