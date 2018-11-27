package com.questmark.entity.components.enemy;

import com.badlogic.ashley.core.Component;

/**
 * Represents the frequency an enemy changes actions with a given movement.
 *
 * @author Ming Li
 */
public final class MovementFrequencyComponent implements Component {

    // in seconds
    public final float frequency;

    public MovementFrequencyComponent(float frequency) {
        this.frequency = frequency;
    }

}
