package com.questmark.entity.entities.testenemies;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.questmark.entity.components.*;
import com.questmark.entity.components.enemy.CircularMovementComponent;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.enemy.MovementFrequencyComponent;
import com.questmark.util.Resources;

public class Dog extends Entity {

    public Dog(Vector2 position, Resources res) {
        this.add(new EnemyComponent());
        this.add(new TextureComponent(res.getSingleTexture("dog")));
        this.add(new PositionComponent(position));
        this.add(new PreviousPositionComponent(position));
        this.add(new VelocityComponent(0, 0));
        this.add(new SpeedComponent(50.f));
        this.add(new BoundingBoxComponent(12, 12));
        this.add(new CircularMovementComponent());
        this.add(new MovementFrequencyComponent(0.3f));
    }

}
