package com.questmark.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

/**
 * Represents the bounding box of an entity for AABB collision detection. The bounding
 * box is usually smaller than the dimensions of an entity's texture dimensions.
 *
 * @author Ming Li
 */
public class BoundingBoxComponent implements Component {

    public Rectangle bounds;

    public BoundingBoxComponent(int w, int h) throws IllegalArgumentException {
        if (w < 0 || h < 0) throw new IllegalArgumentException("Width and height cannot be negative.");
        // bb component doesn't need to know about position initially
        this.bounds = new Rectangle(0, 0, w, h);
    }

    public BoundingBoxComponent(Rectangle bounds) {
        this.bounds = bounds;
    }

}
