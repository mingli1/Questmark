package com.questmark.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents the velocity of an entity in (dx,dy) as speed in either dimension.
 * Can be defined as either a {@link Vector2} or two floats dx, dy.
 *
 * @author Ming Li
 */
public final class VelocityComponent implements Component {

    public float dx;
    public float dy;

    public VelocityComponent(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public VelocityComponent(Vector2 vel) {
        this.setVel(vel);
    }

    public void setVel(Vector2 vel) {
        this.dx = vel.x;
        this.dy = vel.y;
    }

    public Vector2 getVel() {
        return new Vector2(dx, dy);
    }

}
