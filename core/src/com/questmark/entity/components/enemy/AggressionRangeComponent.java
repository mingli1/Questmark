package com.questmark.entity.components.enemy;

import com.badlogic.ashley.core.Component;

/**
 * Represents the aggression range of an enemy if it is aggressive, meaning
 * the player will be attacked by the enemy if its position is within this range
 * with respect to the enemy's position.
 *
 * @author Ming Li
 */
public class AggressionRangeComponent implements Component {

    public float range;

    public AggressionRangeComponent(float range) {
        this.range = range;
    }

}
