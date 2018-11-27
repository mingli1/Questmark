package com.questmark.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents the position of an entity in (x,y) coordinates on the map before a delta time velocity update.
 * Used for entity collision detection.
 * Can be defined as either a {@link Vector2} or two floats x, y.
 *
 * @author Ming Li
 */
public final class PreviousPositionComponent implements Component {

    public float x;
    public float y;

    public PreviousPositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PreviousPositionComponent(Vector2 pos) {
        this.setPrevPos(pos);
    }

    public void setPrevPos(Vector2 pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    public Vector2 getPrevPos() {
        return new Vector2(x, y);
    }

}
