package com.questmark.entity.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.questmark.entity.components.*;
import com.questmark.entity.components.enemy.AggressionComponent;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.enemy.MovementFrequencyComponent;
import com.questmark.entity.components.enemy.SourcePositionComponent;
import com.questmark.util.Resources;

public class AStarEntity extends Entity {

    public AStarEntity(Vector2 position, Resources res) {
        TextureRegion texture = res.getSingleTexture("colonel");
        this.add(new EnemyComponent());
        this.add(new TextureComponent(texture));
        this.add(new DimensionComponent(texture.getRegionWidth(), texture.getRegionHeight()));
        this.add(new PositionComponent(position));
        this.add(new PreviousPositionComponent(position));
        this.add(new VelocityComponent(0, 0));
        this.add(new SpeedComponent(12.f));
        this.add(new BoundingBoxComponent(12, 16));
        this.add(new MovementFrequencyComponent(0.1f));
        this.add(new AggressionComponent(50.f));
        this.add(new SourcePositionComponent(position.x, position.y));
    }


}
