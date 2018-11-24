package com.questmark.entity.entities.testenemies;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.questmark.entity.components.*;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.enemy.MovementFrequencyComponent;
import com.questmark.entity.components.enemy.VerticalMovementComponent;
import com.questmark.util.Resources;

public class Nil extends Entity {

    public Nil(Vector2 position, Resources res) {
        this.add(new EnemyComponent());
        this.add(new TextureComponent(res.getSingleTexture("nil")));
        this.add(new PositionComponent(position));
        this.add(new PreviousPositionComponent(position));
        this.add(new VelocityComponent(0, 0));
        this.add(new SpeedComponent(13.f));
        this.add(new BoundingBoxComponent(12, 12));
        this.add(new VerticalMovementComponent());
        this.add(new MovementFrequencyComponent(1.2f));
    }

}
