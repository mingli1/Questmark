package com.questmark.entity.systems.enemy;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.enemy.AggressionComponent;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.SpeedComponent;
import com.questmark.entity.components.VelocityComponent;
import com.questmark.entity.components.enemy.RandomMovementComponent;
import com.questmark.input.Direction;

import java.util.HashMap;
import java.util.Map;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the movement of enemies. In this system,
 * enemies move randomly by changing actions (either idle or movement in a random direction)
 *
 * @author Ming Li
 */
public class RandomMovementSystem extends IteratingSystem {

    private Map<Entity, Float> timers;

    public RandomMovementSystem() {
        super(Family.all(EnemyComponent.class, RandomMovementComponent.class).get());
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
        AggressionComponent agg = Mapper.AGGRESSION_MAPPER.get(entity);

        if (agg != null) {
            if (agg.atSource) handleMovement(entity, dt);
        } else handleMovement(entity, dt);
    }

    private void handleMovement(Entity entity, float dt) {
        timers.put(entity, timers.get(entity) + dt);

        VelocityComponent vel = Mapper.VEL_MAPPER.get(entity);
        SpeedComponent mag = Mapper.SPEED_MAPPER.get(entity);
        RandomMovementComponent rand = Mapper.RAND_MOVE_MAPPER.get(entity);

        // change action every frequency seconds
        if (timers.get(entity) > rand.freq) {
            int action = MathUtils.random(4);
            if (action == 4) vel.v.x = vel.v.y = 0.f;
            else vel.move(Direction.getDir(action), mag.speed);
            timers.put(entity, timers.get(entity) - rand.freq);
        }
    }

}
