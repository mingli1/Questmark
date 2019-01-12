package com.questmark.entity.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.questmark.entity.Mapper
import com.questmark.entity.components.PlayerComponent
import com.questmark.entity.components.SpeedComponent
import com.questmark.entity.components.VelocityComponent
import com.questmark.input.Direction
import com.questmark.input.KeyInputHandler

/**
 * A LibGDX Ashley [EntitySystem] that handles the updating of the player entity's
 * position based on keyboard input events.
 *
 * @author Ming Li
 */
class KeyInputSystem : EntitySystem(), KeyInputHandler {

    private var player: Entity? = null

    override fun addedToEngine(engine: Engine?) {
        player = engine!!.getEntitiesFor(Family.all(PlayerComponent::class.java).get()).get(0)
    }

    override fun move(direction: Direction) {
        val velocity = Mapper.VEL_MAPPER!!.get(player!!)
        val speed = Mapper.SPEED_MAPPER!!.get(player!!)
        velocity.move(direction, speed.speed)
    }

    override fun stop(direction: Direction) {
        val velocity = Mapper.VEL_MAPPER!!.get(player!!)
        when (direction) {
            Direction.Up -> if (velocity.v.y > 0) velocity.v.y = 0f
            Direction.Down -> if (velocity.v.y < 0) velocity.v.y = 0f
            Direction.Left -> if (velocity.v.x < 0) velocity.v.x = 0f
            Direction.Right -> if (velocity.v.x > 0) velocity.v.x = 0f
        }
    }

}
