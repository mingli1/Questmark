package com.questmark.entity.components.enemy

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

/**
 * Represents the original position of an entity when it first spawns. Used
 * mainly for enemies to store the location to return to if lost.
 *
 * @author Ming Li
 */
class SourcePositionComponent(sourceX: Float, sourceY: Float) : Component {

    val s: Vector2 = Vector2(sourceX, sourceY)

    constructor(source: Vector2) : this(source.x, source.y)

}
