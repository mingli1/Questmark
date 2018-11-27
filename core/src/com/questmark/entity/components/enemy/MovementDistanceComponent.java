package com.questmark.entity.components.enemy;

import com.badlogic.ashley.core.Component;

/**
 * Represents the distance from an enemy's current position before changing actions
 * to a target position depending on the movement system.
 *
 * @author Ming Li
 */
public final class MovementDistanceComponent implements Component {

    public final float dist;

    public MovementDistanceComponent(float dist) {
        this.dist = dist;
    }

}
