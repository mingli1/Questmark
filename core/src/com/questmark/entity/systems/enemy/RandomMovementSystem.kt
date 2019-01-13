package com.questmark.entity.systems.enemy

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.questmark.entity.Mapper
import com.questmark.entity.components.enemy.EnemyComponent
import com.questmark.entity.components.enemy.RandomMovementComponent
import com.questmark.input.Direction

import java.util.HashMap

/**
 * A LibGDX Ashley [EntitySystem] that handles the movement of enemies. In this system,
 * enemies move randomly by changing actions (either idle or movement in a random direction)
 *
 * @author Ming Li
 */
class RandomMovementSystem : IteratingSystem(Family.all(EnemyComponent::class.java, RandomMovementComponent::class.java).get()) {

    private var timers: MutableMap<Entity, Float>? = null

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        timers = HashMap()
        for (e in this.entities) {
            timers!![e] = 0f
        }
    }

    override fun processEntity(entity: Entity, dt: Float) {
        val agg = Mapper.AGGRESSION_MAPPER.get(entity)

        if (agg != null) {
            if (agg.atSource) handleMovement(entity, dt)
        } else
            handleMovement(entity, dt)
    }

    private fun handleMovement(entity: Entity, dt: Float) {
        timers!![entity] = timers!![entity]!! + dt

        val vel = Mapper.VEL_MAPPER.get(entity)
        val mag = Mapper.SPEED_MAPPER.get(entity)
        val rand = Mapper.RAND_MOVE_MAPPER.get(entity)

        // change action every frequency seconds
        if (timers!![entity]!! > rand.freq) {
            val action = MathUtils.random(4)
            if (action == 4) {
                vel.v.y = 0f
                vel.v.x = vel.v.y
            } else
                vel.move(Direction.getDir(action)!!, mag.speed)
            timers!![entity] = timers!![entity]!! - rand.freq
        }
    }

}
