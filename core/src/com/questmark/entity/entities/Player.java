package com.questmark.entity.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.questmark.entity.components.*;
import com.questmark.util.Resources;

/**
 * Represents the protagonist of the game.
 *
 * <p>
 *     The player is a LibGDX Ashley {@link Entity} that is defined by a set of
 *     components {@link com.badlogic.ashley.core.Component} as part of the main ECS.
 *     Only one instance of this object should exist at once.
 * </p>
 *
 * @author Ming Li
 */
public final class Player extends Entity {

    public Player(Vector2 position, Resources res) {
        this.add(new PlayerComponent());
        this.add(new TextureComponent(res.getSingleTexture("player")));
        this.add(new PositionComponent(position));
        this.add(new PreviousPositionComponent(position));
        this.add(new VelocityComponent(0, 0));
        this.add(new SpeedComponent(25.f));
        this.add(new BoundingBoxComponent(12, 16));
    }

}
