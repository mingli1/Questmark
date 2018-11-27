package com.questmark.entity.components.enemy;

import com.badlogic.ashley.core.Component;

/**
 * Represents the original position of an entity when it first spawns. Used
 * mainly for enemies to store the location to return to if lost.
 *
 * @author Ming Li
 */
public final class SourcePositionComponent implements Component {

    public final float sourceX;
    public final float sourceY;

    public SourcePositionComponent(float sourceX, float sourceY) {
        this.sourceX = sourceX;
        this.sourceY = sourceY;
    }

}
