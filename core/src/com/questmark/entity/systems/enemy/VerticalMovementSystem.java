package com.questmark.entity.systems.enemy;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.SpeedComponent;
import com.questmark.entity.components.VelocityComponent;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.enemy.MovementFrequencyComponent;
import com.questmark.entity.components.enemy.VerticalMovementComponent;

import java.util.HashMap;
import java.util.Map;

public class VerticalMovementSystem extends IteratingSystem {

    private Map<Entity, Float> timers;

    public VerticalMovementSystem() {
        super(Family.all(EnemyComponent.class, VerticalMovementComponent.class,
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
            if (vel.dy == 0.f) {
                vel.dy = MathUtils.randomBoolean() ? mag.speed : -mag.speed;
                return;
            }
            if (vel.dy > 0) vel.dy = -mag.speed;
            else vel.dy = mag.speed;
            timers.put(entity, timers.get(entity) - freq.frequency);
        }
    }

}
