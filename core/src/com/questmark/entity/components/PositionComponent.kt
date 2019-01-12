package com.questmark.entity.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

/**
 * Represents the position of an entity in (x,y) coordinates on the map.
 * Can be defined as either a [Vector2] or two floats x, y.
 *
 * @author Ming Li
 */
class PositionComponent(x: Float, y: Float) : Component {

    var p: Vector2 = Vector2(x, y)

    constructor(pos: Vector2) : this(pos.x, pos.y)

}
