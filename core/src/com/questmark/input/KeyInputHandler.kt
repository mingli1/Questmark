package com.questmark.input

/**
 * Offers public methods for game commands triggered by key events such as
 * moving, running, attacking, using a skill, etc. Specific to the player entity.
 *
 * @author Ming Li
 */
interface KeyInputHandler {

    /**
     * Moves the player in a given direction
     *
     * @param direction
     */
    fun move(direction: Direction)

    /**
     * Stops the player's movement in a given direction
     */
    fun stop(direction: Direction)

}
