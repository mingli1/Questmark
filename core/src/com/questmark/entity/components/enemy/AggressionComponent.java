package com.questmark.entity.components.enemy;

import com.badlogic.ashley.core.Component;

/**
 * A {@link Component} of an enemy entity that represents an enemy's aggression, meaning
 * if an enemy will follow and attack the player if in range. Contains a value representing
 * the radius of the effective aggression circle and whether the entity is at its source location.
 *
 * <p>If aggression range is -1, then the enemy has global aggression.</p>
 *
 * <p>If an entity has a movement system and is also aggressive, then when provoked, the entity will
 * begin A* pathfinding towards to the player and once the player is out of aggression range, it will return
 * to the source location and continue its movement system.</p>
 *
 * @author Ming Li
 */
public final class AggressionComponent implements Component {

    public float range;
    public boolean atSource;

    public AggressionComponent(float range) {
        this.range = range;
        this.atSource = true;
    }

}
