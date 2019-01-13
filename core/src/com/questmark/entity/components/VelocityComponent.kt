package com.questmark.entity.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.questmark.input.Direction

/**
 * Represents the velocity of an entity in (dx,dy) as speed in either dimension.
 * Can be defined as either a [Vector2] or two floats dx, dy.
 *
 * @author Ming Li
 */
class VelocityComponent(dx: Float = 0f, dy: Float = 0f) : Component {

    var v: Vector2 = Vector2(dx, dy)

    constructor(vel: Vector2 = Vector2(0f, 0f)) : this(vel.x, vel.y)

    /**
     * Sets velocity given an direction and magnitude.
     *
     * @param dir direction of movement
     * @param mag magnitude of movement
     */
    fun move(dir: Direction, mag: Float) {
        when (dir) {
            Direction.Up -> v.y = mag
            Direction.Down -> v.y = -mag
            Direction.Left -> v.x = -mag
            Direction.Right -> v.x = mag
        }
    }

}
