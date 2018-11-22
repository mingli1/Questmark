package com.questmark.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Represents the graphical representation of an entity as a {@link TextureRegion}.
 *
 * @author Ming Li
 */
public final class TextureComponent implements Component {

    public TextureRegion texture;

    public TextureComponent(TextureRegion texture) {
        this.setTexture(texture);
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

}
