package com.restonic4.citadel.api;

/**
This interface is used to set up mods. It provides useful methods.
 */
public interface Mod {
    /**
    Gets called on the start phase of your mod.
     */
    void onStart();

    /**
    Gets called every frame.
     */
    void onUpdate();

    /**
    Gets called on the stop phase of the engine.
     */
    void onStop();
}
