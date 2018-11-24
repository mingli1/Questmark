package com.questmark.entity.systems.enemy;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.PlayerComponent;
import com.questmark.entity.components.PositionComponent;
import com.questmark.entity.components.SpeedComponent;
import com.questmark.entity.components.VelocityComponent;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.enemy.NaiveFollowMovementComponent;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the movement of enemies. In this system,
 * enemies naively follow the player without consideration for obstacles in the path.
 *
 * @author Ming Li
 */
public class NaiveFollowMovementSystem extends IteratingSystem {

    private Entity player;

    public NaiveFollowMovementSystem() {
        super(Family.all(EnemyComponent.class, NaiveFollowMovementComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).get(0);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = Mapper.POS_MAPPER.get(entity);
        VelocityComponent vel = Mapper.VEL_MAPPER.get(entity);
        SpeedComponent mag = Mapper.SPEED_MAPPER.get(entity);
        PositionComponent playerPos = Mapper.POS_MAPPER.get(player);

        if (pos.x < playerPos.x) vel.dx = mag.speed;
        else if (pos.x > playerPos.x) vel.dx = -mag.speed;
        else vel.dx = 0;

        if (pos.y < playerPos.y) vel.dy = mag.speed;
        else if (pos.y > playerPos.y) vel.dy = -mag.speed;
        else vel.dy = 0;
    }

}
