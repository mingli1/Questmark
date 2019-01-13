package com.questmark.entity.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.questmark.anim.Animation
import com.questmark.entity.components.*
import com.questmark.util.Resources

/**
 * Represents the protagonist of the game.
 *
 * The player is a LibGDX Ashley [Entity] that is defined by a set of
 * components [com.badlogic.ashley.core.Component] as part of the main ECS.
 * Only one instance of this object should exist at once.
 *
 *
 * @author Ming Li
 */
class Player(position: Vector2, res: Resources) : Entity() {

    init {
        val texture = res.getSingleTexture("luffy")
        this.add(PlayerComponent())
        this.add(AnimationComponent(Animation(texture!!)))
        this.add(DimensionComponent(texture!!.regionWidth, texture.regionHeight))
        this.add(PositionComponent(position))
        this.add(PreviousPositionComponent(position))
        this.add(VelocityComponent())
        this.add(SpeedComponent(25f))
        this.add(BoundingBoxComponent(12, 16))
    }

}
