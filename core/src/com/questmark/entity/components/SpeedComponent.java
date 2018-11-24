package com.questmark.entity.components;

import com.badlogic.ashley.core.Component;

/**
 * A {@link Component} that represents how fast an entity can move. In other words,
 * the magnitude of the entity's velocity.
 *
 * @author Ming Li
 */
public class SpeedComponent implements Component {

    public float speed;

    public SpeedComponent(float speed) throws IllegalArgumentException {
        if (speed < 0) {
            throw new IllegalArgumentException("Speed cannot be negative");
        }
        this.speed = speed;
    }

}
