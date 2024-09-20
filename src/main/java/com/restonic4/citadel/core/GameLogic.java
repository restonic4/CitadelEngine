package com.restonic4.citadel.core;

/**
 * This contains the game logic.
 */
public interface GameLogic {
    /**
     * Gets called when the engine loads and it's ready to execute your code.
     */
    void start();

    /**
     * Gets called every frame after start has been called.
     */
    void update();
}
