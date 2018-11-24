package com.questmark.entity.components;

import com.badlogic.ashley.core.Component;

/**
 * Represents the size of an entity as width and height.
 *
 * @author Ming Li
 */
public class DimensionComponent implements Component {

    public int width;
    public int height;

    public DimensionComponent(int width, int height) throws IllegalArgumentException {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Dimensions cannot be negative");
        }
        this.width = width;
        this.height = height;
    }

}
