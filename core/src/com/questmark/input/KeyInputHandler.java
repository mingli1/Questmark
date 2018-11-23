package com.questmark.input;

/**
 * Offers public methods for game commands triggered by key events such as
 * moving, running, attacking, using a skill, etc. Specific to the player entity.
 *
 * @author Ming Li
 */
public interface KeyInputHandler {

    /**
     * Moves the player in a given direction
     *
     * @param direction
     */
    void move(Direction direction);

    /**
     * Stops the player's movement in a given direction
     */
    void stop(Direction direction);

}
