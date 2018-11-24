package com.questmark.entity.entities.testenemies;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.questmark.entity.components.*;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.enemy.NaiveFollowMovementComponent;
import com.questmark.util.Resources;

public class Cat extends Entity {

    public Cat(Vector2 position, Resources res) {
        this.add(new EnemyComponent());
        this.add(new TextureComponent(res.getSingleTexture("cat")));
        this.add(new PositionComponent(position));
        this.add(new PreviousPositionComponent(position));
        this.add(new VelocityComponent(0, 0));
        this.add(new SpeedComponent(7.f));
        this.add(new BoundingBoxComponent(12, 12));
        this.add(new NaiveFollowMovementComponent());
    }

}
