package com.questmark.entity.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion

/**
 * Represents the graphical representation of an entity as a [TextureRegion].
 *
 * @author Ming Li
 */
class TextureComponent @Throws(IllegalArgumentException::class)
constructor(var texture: TextureRegion?) : Component {

    init {
        if (texture == null) {
            throw IllegalArgumentException("Texture cannot be null")
        }
    }

}
