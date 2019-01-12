package com.questmark.entity.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Rectangle

/**
 * Represents the bounding box of an entity for AABB collision detection. The bounding
 * box is usually smaller than the dimensions of an entity's texture dimensions.
 *
 * @author Ming Li
 */
class BoundingBoxComponent : Component {

    var bounds: Rectangle

    @Throws(IllegalArgumentException::class)
    constructor(w: Int, h: Int) {
        if (w < 0 || h < 0) throw IllegalArgumentException("Width and height cannot be negative.")
        // bb component doesn't need to know about position initially
        this.bounds = Rectangle(0f, 0f, w.toFloat(), h.toFloat())
    }

    constructor(bounds: Rectangle) {
        this.bounds = bounds
    }

}
