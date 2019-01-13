package com.questmark.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.questmark.entity.Mapper
import com.questmark.entity.components.PositionComponent
import com.questmark.entity.components.PreviousPositionComponent
import com.questmark.entity.components.VelocityComponent

/**
 * A LibGDX Ashley [EntitySystem] that handles the updating of position components
 * based on velocity components.
 *
 * @author Ming Li
 */
class MovementSystem :
        IteratingSystem(
                Family.all(PositionComponent::class.java, VelocityComponent::class.java,
                        PreviousPositionComponent::class.java).get()
        ) {

    override fun processEntity(entity: Entity, dt: Float) {
        val position = Mapper.POS_MAPPER.get(entity)
        val velocity = Mapper.VEL_MAPPER.get(entity)
        val prevPosition = Mapper.PREV_POS_MAPPER.get(entity)

        prevPosition.p.set(position.p)
        position.p.x += velocity.v.x * dt
        position.p.y += velocity.v.y * dt
    }

}
