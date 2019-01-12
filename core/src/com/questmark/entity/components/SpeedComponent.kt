package com.questmark.entity.components

import com.badlogic.ashley.core.Component

/**
 * A [Component] that represents how fast an entity can move. In other words,
 * the magnitude of the entity's velocity.
 *
 * @author Ming Li
 */
class SpeedComponent @Throws(IllegalArgumentException::class)
constructor(var speed: Float) : Component {

    init {
        if (speed < 0) {
            throw IllegalArgumentException("Speed cannot be negative")
        }
    }

}
