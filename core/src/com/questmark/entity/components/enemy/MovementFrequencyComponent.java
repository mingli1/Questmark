package com.questmark.entity.components.enemy;

import com.badlogic.ashley.core.Component;

/**
 * Represents the frequency an enemy changes actions with random movement.
 *
 * @author Ming Li
 */
public class MovementFrequencyComponent implements Component {

    // in seconds
    public float frequency;

    public MovementFrequencyComponent(float frequency) {
        this.frequency = frequency;
    }

}
