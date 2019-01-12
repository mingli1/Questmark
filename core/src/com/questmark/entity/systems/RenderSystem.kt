package com.questmark.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.questmark.entity.Mapper
import com.questmark.entity.components.PositionComponent
import com.questmark.entity.components.TextureComponent

/**
 * A LibGDX Ashley [EntitySystem] that handles the rendering of entities
 * based on texture and position components.
 *
 * @author Ming Li
 */
class RenderSystem(private val batch: Batch) :
        IteratingSystem(Family.all(TextureComponent::class.java, PositionComponent::class.java).get()) {

    override fun processEntity(entity: Entity, dt: Float) {
        val texture = Mapper.TEXTURE_MAPPER!!.get(entity)
        val position = Mapper.POS_MAPPER!!.get(entity)

        batch.draw(texture.texture, position.p.x, position.p.y)
    }

}
