package com.questmark.entity.systems.enemy;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.SpeedComponent;
import com.questmark.entity.components.VelocityComponent;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.enemy.HorizontalMovementComponent;
import com.questmark.entity.components.enemy.MovementFrequencyComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the movement of enemies. In this system,
 * enemies move back and forth in a horizontal path determined by frequency of direction change.
 *
 * @author Ming Li
 */
public class HorizontalMovementSystem extends IteratingSystem {

    private Map<Entity, Float> timers;

    public HorizontalMovementSystem() {
        super(Family.all(EnemyComponent.class, HorizontalMovementComponent.class,
                MovementFrequencyComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        timers = new HashMap<Entity, Float>();
        for (Entity e : this.getEntities()) {
            timers.put(e, 0.f);
        }
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        timers.put(entity, timers.get(entity) + dt);

        VelocityComponent vel = Mapper.VEL_MAPPER.get(entity);
        SpeedComponent mag = Mapper.SPEED_MAPPER.get(entity);
        MovementFrequencyComponent freq = Mapper.MOVE_FREQ_MAPPER.get(entity);

        // change action every frequency seconds
        if (timers.get(entity) > freq.frequency) {
            if (vel.dx == 0.f) {
                vel.dx = MathUtils.randomBoolean() ? mag.speed : -mag.speed;
                return;
            }
            if (vel.dx > 0) vel.dx = -mag.speed;
            else vel.dx = mag.speed;
            timers.put(entity, timers.get(entity) - freq.frequency);
        }
    }

}
