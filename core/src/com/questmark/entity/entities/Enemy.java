package com.questmark.entity.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.questmark.entity.components.*;
import com.questmark.entity.components.enemy.CircularMovementComponent;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.enemy.MovementFrequencyComponent;
import com.questmark.entity.components.enemy.RandomMovementComponent;
import com.questmark.util.Resources;

/**
 * A temporary enemy
 *
 * @author Ming Li
 */
public class Enemy extends Entity {

    public Enemy(Vector2 position, Resources res) {
        this.add(new EnemyComponent());
        this.add(new TextureComponent(res.getSingleTexture("colonel")));
        this.add(new PositionComponent(position));
        this.add(new PreviousPositionComponent(position));
        this.add(new VelocityComponent(0, 0));
        this.add(new SpeedComponent(15.f));
        this.add(new BoundingBoxComponent(12, 16));
        this.add(new CircularMovementComponent());
        this.add(new MovementFrequencyComponent(0.8f));
    }

}
