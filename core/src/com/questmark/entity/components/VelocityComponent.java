package com.questmark.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.questmark.input.Direction;

/**
 * Represents the velocity of an entity in (dx,dy) as speed in either dimension.
 * Can be defined as either a {@link Vector2} or two floats dx, dy.
 *
 * @author Ming Li
 */
public final class VelocityComponent implements Component {

    public Vector2 v;

    public VelocityComponent(float dx, float dy) {
        v = new Vector2(dx, dy);
    }

    public VelocityComponent(Vector2 vel) {
        this(vel.x, vel.y);
    }

    /**
     * Sets velocity given an direction and magnitude.
     *
     * @param dir direction of movement
     * @param mag magnitude of movement
     */
    public void move(Direction dir, float mag) {
        switch (dir) {
            case Up:
                v.y = mag;
                break;
            case Down:
                v.y = -mag;
                break;
            case Left:
                v.x = -mag;
                break;
            case Right:
                v.x = mag;
                break;
        }
    }

}
