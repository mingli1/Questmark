package com.questmark.entity.components.enemy;

import com.badlogic.ashley.core.Component;

/**
 * Represents an enemy that moves in a circle clockwise or counterclockwise,
 * changing direction in a given frequency.
 *
 * @author Ming Li
 */
public final class CircularMovementComponent implements Component {

    public final float dist;

    public CircularMovementComponent(float dist) {
        this.dist = dist;
    }

}
