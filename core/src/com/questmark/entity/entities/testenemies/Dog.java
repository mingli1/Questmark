package com.questmark.entity.entities.testenemies;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.questmark.entity.components.*;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.enemy.NaiveFollowMovementComponent;
import com.questmark.util.Resources;

public class Dog extends Entity {

    public Dog(Vector2 position, Resources res) {
        TextureRegion texture = res.getSingleTexture("dog");
        this.add(new EnemyComponent());
        this.add(new TextureComponent(texture));
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(texture.getRegionWidth(), texture.getRegionHeight()));
        this.add(new PreviousPositionComponent(position));
        this.add(new VelocityComponent(0, 0));
        this.add(new SpeedComponent(10.f));
        this.add(new BoundingBoxComponent(12, 12));
        this.add(new NaiveFollowMovementComponent());
    }

}
