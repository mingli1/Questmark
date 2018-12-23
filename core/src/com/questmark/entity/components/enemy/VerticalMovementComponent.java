package com.questmark.entity.components.enemy;

import com.badlogic.ashley.core.Component;

/**
 * Represents an enemy that moves in a constant vertical motion, changing directions
 * after a certain time period.
 *
 * @author Ming Li
 */
public final class VerticalMovementComponent implements Component {

    public final float dist;

    public VerticalMovementComponent(float dist) {
        this.dist = dist;
    }

}
