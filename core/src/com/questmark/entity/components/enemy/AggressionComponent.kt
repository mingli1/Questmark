package com.questmark.entity.components.enemy

import com.badlogic.ashley.core.Component

/**
 * A [Component] of an enemy entity that represents an enemy's aggression, meaning
 * if an enemy will follow and attack the player if in range. Contains a value representing
 * the radius of the effective aggression circle and whether the entity is at its source location.
 *
 * If aggression range is -1, then the enemy has global aggression.
 *
 * If an entity has a movement system and is also aggressive, then when provoked, the entity will
 * begin A* pathfinding towards to the player and once the player is out of aggression range, it will return
 * to the source location and continue its movement system.
 *
 * @author Ming Li
 */
class AggressionComponent(var range: Float) : Component {

    var atSource: Boolean = false

    init {
        this.atSource = true
    }

}
