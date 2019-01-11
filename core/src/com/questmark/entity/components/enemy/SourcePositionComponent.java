package com.questmark.entity.components.enemy;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents the original position of an entity when it first spawns. Used
 * mainly for enemies to store the location to return to if lost.
 *
 * @author Ming Li
 */
public final class SourcePositionComponent implements Component {

    public final Vector2 s;

    public SourcePositionComponent(float sourceX, float sourceY) {
        s = new Vector2(sourceX, sourceY);
    }

    public SourcePositionComponent(Vector2 source) {
        this(source.x, source.y);
    }

}
