package com.questmark.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents the position of an entity in (x,y) coordinates on the map.
 * Can be defined as either a {@link Vector2} or two floats x, y.
 *
 * @author Ming Li
 */
public final class PositionComponent implements Component {

    public float x;
    public float y;

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PositionComponent(Vector2 pos) {
        this.setPos(pos);
    }

    public void setPos(Vector2 pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    public Vector2 getPos() {
        return new Vector2(x, y);
    }

}
