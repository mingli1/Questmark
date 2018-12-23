package com.questmark.entity.systems.enemy;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.PositionComponent;
import com.questmark.entity.components.SpeedComponent;
import com.questmark.entity.components.VelocityComponent;
import com.questmark.entity.components.enemy.AggressionComponent;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.enemy.MovementDistanceComponent;
import com.questmark.entity.components.enemy.VerticalMovementComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the movement of enemies. In this system,
 * enemies move back and forth in a vertical path determined by frequency of direction change.
 *
 * @author Ming Li
 */
public class VerticalMovementSystem extends IteratingSystem {

    private Map<Entity, Float> targets;

    public VerticalMovementSystem() {
        super(Family.all(EnemyComponent.class, VerticalMovementComponent.class,
                MovementDistanceComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        targets = new HashMap<Entity, Float>();
        for (Entity e : this.getEntities()) {
            PositionComponent pos = Mapper.POS_MAPPER.get(e);
            MovementDistanceComponent dist = Mapper.MOVE_DIST_MAPPER.get(e);
            VelocityComponent vel = Mapper.VEL_MAPPER.get(e);
            SpeedComponent mag = Mapper.SPEED_MAPPER.get(e);
            boolean p = MathUtils.randomBoolean();
            if (p) {
                targets.put(e, pos.p.y + dist.dist);
                vel.v.y = mag.speed;
            }
            else {
                targets.put(e, pos.p.y - dist.dist);
                vel.v.y = -mag.speed;
            }
        }
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        AggressionComponent agg = Mapper.AGGRESSION_MAPPER.get(entity);
        PositionComponent pos = Mapper.POS_MAPPER.get(entity);
        MovementDistanceComponent dist = Mapper.MOVE_DIST_MAPPER.get(entity);

        if (agg != null) {
            if (agg.atSource) {
                if (pos.p.y == targets.get(entity))
                    targets.put(entity, MathUtils.randomBoolean() ? pos.p.y + dist.dist : pos.p.y - dist.dist);
                handleMovement(entity);
            }
        } else handleMovement(entity);
    }

    private void handleMovement(Entity entity) {
        PositionComponent pos = Mapper.POS_MAPPER.get(entity);
        VelocityComponent vel = Mapper.VEL_MAPPER.get(entity);
        SpeedComponent mag = Mapper.SPEED_MAPPER.get(entity);
        MovementDistanceComponent dist = Mapper.MOVE_DIST_MAPPER.get(entity);

        float target = targets.get(entity);

        if (vel.v.y == 0.f) {
            if (pos.p.y < target) vel.v.y = mag.speed;
            else if (pos.p.y > target) vel.v.y = -mag.speed;
        }

        if (vel.v.y > 0 && pos.p.y >= target) {
            targets.put(entity, target - dist.dist);
            vel.v.y = -mag.speed;
        }
        if (vel.v.y < 0 && pos.p.y <= target) {
            targets.put(entity, target + dist.dist);
            vel.v.y = mag.speed;
        }
    }

}
