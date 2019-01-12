package com.questmark.entity.components

import com.badlogic.ashley.core.Component

/**
 * Represents the size of an entity as width and height.
 *
 * @author Ming Li
 */
class DimensionComponent @Throws(IllegalArgumentException::class)
constructor(var width: Int, var height: Int) : Component {

    init {
        if (width < 0 || height < 0) {
            throw IllegalArgumentException("Dimensions cannot be negative")
        }
    }

}
