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
import com.questmark.entity.components.enemy.HorizontalMovementComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the movement of enemies. In this system,
 * enemies move back and forth in a horizontal path determined by frequency of direction change.
 *
 * @author Ming Li
 */
public class HorizontalMovementSystem extends IteratingSystem {

    private Map<Entity, Float> targets;

    public HorizontalMovementSystem() {
        super(Family.all(EnemyComponent.class, HorizontalMovementComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        targets = new HashMap<Entity, Float>();
        for (Entity e : this.getEntities()) {
            PositionComponent pos = Mapper.INSTANCE.getPOS_MAPPER().get(e);
            HorizontalMovementComponent hor = Mapper.INSTANCE.getHOR_MOVE_MAPPER().get(e);
            VelocityComponent vel = Mapper.INSTANCE.getVEL_MAPPER().get(e);
            SpeedComponent mag = Mapper.INSTANCE.getSPEED_MAPPER().get(e);
            boolean p = MathUtils.randomBoolean();
            if (p) {
                targets.put(e, pos.p.x + hor.dist);
                vel.v.x = mag.speed;
            }
            else {
                targets.put(e, pos.p.x - hor.dist);
                vel.v.x = -mag.speed;
            }
        }
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        AggressionComponent agg = Mapper.INSTANCE.getAGGRESSION_MAPPER().get(entity);
        PositionComponent pos = Mapper.INSTANCE.getPOS_MAPPER().get(entity);
        HorizontalMovementComponent hor = Mapper.INSTANCE.getHOR_MOVE_MAPPER().get(entity);

        if (agg != null) {
            if (agg.atSource) {
                if (pos.p.x == targets.get(entity))
                    targets.put(entity, MathUtils.randomBoolean() ? pos.p.x + hor.dist : pos.p.x - hor.dist);
                handleMovement(entity);
            }
        } else handleMovement(entity);
    }

    private void handleMovement(Entity entity) {
        PositionComponent pos = Mapper.INSTANCE.getPOS_MAPPER().get(entity);
        VelocityComponent vel = Mapper.INSTANCE.getVEL_MAPPER().get(entity);
        SpeedComponent mag = Mapper.INSTANCE.getSPEED_MAPPER().get(entity);
        HorizontalMovementComponent hor = Mapper.INSTANCE.getHOR_MOVE_MAPPER().get(entity);

        float target = targets.get(entity);

        if (vel.v.x == 0.f) {
            if (pos.p.x < target) vel.v.x = mag.speed;
            else if (pos.p.x > target) vel.v.x = -mag.speed;
        }

        if (vel.v.x > 0 && pos.p.x >= target) {
            targets.put(entity, target - hor.dist);
            vel.v.x = -mag.speed;
        }
        if (vel.v.x < 0 && pos.p.x <= target) {
            targets.put(entity, target + hor.dist);
            vel.v.x = mag.speed;
        }
    }

}
