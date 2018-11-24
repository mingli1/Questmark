package com.questmark.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.EnemyComponent;
import com.questmark.entity.components.SpeedComponent;
import com.questmark.entity.components.VelocityComponent;
import com.questmark.input.Direction;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the movement of enemies. Enemies
 * can have different movement patterns and even follow the player using A* pathfinding.
 *
 * @author Ming Li
 */
public class EnemyMovementSystem extends IteratingSystem {

    private float time;
    private static final int CHANGE_ACTION = 2;

    public EnemyMovementSystem() {
        super(Family.all(EnemyComponent.class).get());
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (time > CHANGE_ACTION * 10000) time = 0;
        time += dt;
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        VelocityComponent vel = Mapper.VEL_MAPPER.get(entity);
        SpeedComponent mag = Mapper.SPEED_MAPPER.get(entity);

        // change action every CHANGE_ACTION seconds
        if (time % CHANGE_ACTION <= dt) {
            int action = MathUtils.random(4);
            if (action == 4) vel.dx = vel.dy = 0.f;
            else vel.move(Direction.getDir(action), mag.speed);
        }
    }

}
