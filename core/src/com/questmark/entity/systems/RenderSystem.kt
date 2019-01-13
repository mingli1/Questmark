package com.questmark.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.questmark.entity.Mapper
import com.questmark.entity.components.AnimationComponent
import com.questmark.entity.components.PositionComponent

/**
 * A LibGDX Ashley [EntitySystem] that handles the rendering of entities
 * based on animation and position components.
 *
 * @author Ming Li
 */
class RenderSystem(private val batch: Batch) :
        IteratingSystem(Family.all(AnimationComponent::class.java, PositionComponent::class.java).get()) {

    override fun processEntity(entity: Entity, dt: Float) {
        val anim = Mapper.ANIM_MAPPER!!.get(entity)
        val position = Mapper.POS_MAPPER!!.get(entity)

        anim.anim.update(dt)

        batch.draw(anim.anim.currKeyFrame, position.p.x, position.p.y)
    }

}
