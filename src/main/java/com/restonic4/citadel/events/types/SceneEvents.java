package com.restonic4.citadel.events.types;

import com.restonic4.ClientSide;
import com.restonic4.citadel.core.Window;
import com.restonic4.citadel.events.Event;
import com.restonic4.citadel.events.EventFactory;
import com.restonic4.citadel.events.EventResult;
import com.restonic4.citadel.world.Scene;
import com.restonic4.citadel.world.object.GameObject;

/**
 * List of events related to the scene.
 */
@ClientSide
public class SceneEvents {
    /**
     * Gets triggered when the scene gets loaded.
     */
    public static final Event<SceneLoaded> LOADED = EventFactory.createArray(SceneLoaded.class, callbacks -> (scene) -> {
        for (SceneLoaded callback : callbacks) {
            callback.onSceneLoaded(scene);
        }
    });

    @FunctionalInterface
    public interface SceneLoaded {
        void onSceneLoaded(Scene scene);
    }

    /**
     * Gets triggered when the scene gets unloaded.
     */
    public static final Event<SceneUnLoaded> UNLOADED = EventFactory.createArray(SceneUnLoaded.class, callbacks -> (scene) -> {
        for (SceneUnLoaded callback : callbacks) {
            callback.onSceneUnLoaded(scene);
        }
    });

    @FunctionalInterface
    public interface SceneUnLoaded {
        void onSceneUnLoaded(Scene scene);
    }

    /**
     * Gets triggered when an GameObject is being added to the scene.
     */
    public static final Event<SceneAddingObject> ADDING_OBJECT = EventFactory.createArray(SceneAddingObject.class, callbacks -> (scene, gameObject) -> {
        for (SceneAddingObject callback : callbacks) {
            EventResult result = callback.onSceneAddingObject(scene, gameObject);

            if (result == EventResult.CANCELED) {
                return EventResult.CANCELED;
            }
        }

        return EventResult.SUCCEEDED;
    });

    @FunctionalInterface
    public interface SceneAddingObject {
        EventResult onSceneAddingObject(Scene scene, GameObject gameObject);
    }
}
