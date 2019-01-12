package com.questmark.entity.systems.enemy;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.PositionComponent;
import com.questmark.entity.components.SpeedComponent;
import com.questmark.entity.components.VelocityComponent;
import com.questmark.entity.components.enemy.AggressionComponent;
import com.questmark.entity.components.enemy.CircularMovementComponent;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.input.Direction;

import java.util.HashMap;
import java.util.Map;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the movement of enemies. In this system,
 * enemies move in a circle by changing directions every given time interval.
 *
 * @author Ming Li
 */
public class CircularMovementSystem extends EntitySystem {

    private Map<Entity, Vector2> targets;

    private ImmutableArray<Entity> entities;
    // map of entity to its rotation scheme (true for clockwise, false for counterclockwise)
    private Map<Entity, Boolean> rotationDir;
    private Map<Entity, Direction> lastDir;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(EnemyComponent.class, CircularMovementComponent.class).get());
        targets = new HashMap<Entity, Vector2>();
        rotationDir = new HashMap<Entity, Boolean>();
        lastDir = new HashMap<Entity, Direction>();

        for (Entity e : entities) {
            PositionComponent pos = Mapper.INSTANCE.getPOS_MAPPER().get(e);
            targets.put(e, new Vector2(pos.p.x, pos.p.y));
            rotationDir.put(e, MathUtils.randomBoolean());
            lastDir.put(e, Direction.Up);
        }
    }

    @Override
    public void update(float dt) {
        for (Entity e : entities) {
            AggressionComponent agg = Mapper.INSTANCE.getAGGRESSION_MAPPER().get(e);

            if (agg != null) {
                if (agg.atSource) handleMovement(e);
            }
            else handleMovement(e);
        }
    }

    private void handleMovement(Entity e) {
        PositionComponent pos = Mapper.INSTANCE.getPOS_MAPPER().get(e);
        VelocityComponent vel = Mapper.INSTANCE.getVEL_MAPPER().get(e);
        SpeedComponent mag = Mapper.INSTANCE.getSPEED_MAPPER().get(e);
        CircularMovementComponent cir = Mapper.INSTANCE.getCIR_MOVE_MAPPER().get(e);

        Direction prevDir = lastDir.get(e);
        Vector2 target = targets.get(e);

        // entity is stopped during movement
        if (vel.v.x == 0.f) {
            if (pos.p.x < target.x) vel.v.x = mag.speed;
            else if (pos.p.x > target.x) vel.v.x = -mag.speed;
        }
        if (vel.v.y == 0.f) {
            if (pos.p.y < target.y) vel.v.y = mag.speed;
            else if (pos.p.y > target.y) vel.v.y = -mag.speed;
        }

        if ((prevDir == Direction.Up && pos.p.y >= target.y) ||
                (prevDir == Direction.Right && pos.p.x >= target.x) ||
                (prevDir == Direction.Down && pos.p.y <= target.y) ||
                (prevDir == Direction.Left && pos.p.x <= target.x)) {
            pos.p.x = target.x;
            pos.p.y = target.y;
            vel.v.x = vel.v.y = 0.f;

            if (rotationDir.get(e)) {
                switch (lastDir.get(e)) {
                    case Up:
                        vel.v.x = mag.speed;
                        lastDir.put(e, Direction.Right);
                        targets.put(e, target.set(target.x + cir.dist, target.y));
                        break;
                    case Right:
                        vel.v.y = -mag.speed;
                        lastDir.put(e, Direction.Down);
                        targets.put(e, target.set(target.x, target.y - cir.dist));
                        break;
                    case Down:
                        vel.v.x = -mag.speed;
                        lastDir.put(e, Direction.Left);
                        targets.put(e, target.set(target.x - cir.dist, target.y));
                        break;
                    case Left:
                        vel.v.y = mag.speed;
                        lastDir.put(e, Direction.Up);
                        targets.put(e, target.set(target.x, target.y + cir.dist));
                        break;
                }
            } else {
                switch (lastDir.get(e)) {
                    case Up:
                        vel.v.x = -mag.speed;
                        lastDir.put(e, Direction.Left);
                        targets.put(e, target.set(target.x - cir.dist, target.y));
                        break;
                    case Left:
                        vel.v.y = -mag.speed;
                        lastDir.put(e, Direction.Down);
                        targets.put(e, target.set(target.x, target.y - cir.dist));
                        break;
                    case Down:
                        vel.v.x = mag.speed;
                        lastDir.put(e, Direction.Right);
                        targets.put(e, target.set(target.x + cir.dist, target.y));
                        break;
                    case Right:
                        vel.v.y = mag.speed;
                        lastDir.put(e, Direction.Up);
                        targets.put(e, target.set(target.x, target.y + cir.dist));
                        break;
                }
            }
        }
    }

}
