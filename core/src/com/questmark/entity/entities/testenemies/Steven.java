package com.questmark.entity.entities.testenemies;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.questmark.entity.components.*;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.enemy.NaiveFollowMovementComponent;
import com.questmark.util.Resources;

public class Steven extends Entity {

    public Steven(Vector2 position, Resources res) {
        TextureRegion texture = res.getSingleTexture("enemy");
        this.add(new EnemyComponent());
        this.add(new TextureComponent(texture));
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(texture.getRegionWidth(), texture.getRegionHeight()));
        this.add(new PreviousPositionComponent(position));
        this.add(new VelocityComponent(0, 0));
        this.add(new SpeedComponent(15.f));
        this.add(new BoundingBoxComponent(12, 12));
        this.add(new NaiveFollowMovementComponent());
    }

}
