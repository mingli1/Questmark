package com.questmark.entity.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.questmark.anim.Animation
import com.questmark.entity.components.AnimationComponent
import com.questmark.entity.components.DimensionComponent
import com.questmark.entity.components.PositionComponent
import com.questmark.util.Resources

class NormalAnim(position: Vector2, res: Resources) : Entity() {

    init {
        val anim = Animation(Array(res.getMultipleTextures("normal_anim")), 0.3f)
        this.add(AnimationComponent(anim))
        this.add(DimensionComponent(16, 16))
        this.add(PositionComponent(position))
    }

}