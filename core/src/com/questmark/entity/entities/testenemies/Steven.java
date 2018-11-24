package com.questmark.entity.entities.testenemies;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.questmark.entity.components.*;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.enemy.HorizontalMovementComponent;
import com.questmark.entity.components.enemy.MovementFrequencyComponent;
import com.questmark.util.Resources;

public class Steven extends Entity {

    public Steven(Vector2 position, Resources res) {
        this.add(new EnemyComponent());
        this.add(new TextureComponent(res.getSingleTexture("enemy")));
        this.add(new PositionComponent(position));
        this.add(new PreviousPositionComponent(position));
        this.add(new VelocityComponent(0, 0));
        this.add(new SpeedComponent(15.f));
        this.add(new BoundingBoxComponent(12, 12));
        this.add(new HorizontalMovementComponent());
        this.add(new MovementFrequencyComponent(2.5f));
    }

}
