package com.questmark.entity.components.enemy;

import com.badlogic.ashley.core.Component;

/**
 * A {@link Component} of an enemy entity that represents an enemy's aggression, meaning
 * if an enemy will follow and attack the player if in range. Contains a value representing
 * the radius of the effective aggression circle and whether the entity is at its source location.
 *
 * If aggression range is -1, then the enemy has global aggression.
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
