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
import com.questmark.entity.components.enemy.CircularMovementComponent;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.enemy.MovementDistanceComponent;
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
        entities = engine.getEntitiesFor(Family.all(EnemyComponent.class, CircularMovementComponent.class,
                MovementDistanceComponent.class).get());
        targets = new HashMap<Entity, Vector2>();
        rotationDir = new HashMap<Entity, Boolean>();
        lastDir = new HashMap<Entity, Direction>();

        for (Entity e : entities) {
            PositionComponent pos = Mapper.POS_MAPPER.get(e);
            targets.put(e, new Vector2(pos.x, pos.y));
            rotationDir.put(e, MathUtils.randomBoolean());
            lastDir.put(e, Direction.Up);
        }
    }

    @Override
    public void update(float dt) {
        for (Entity e : entities) {
            PositionComponent pos = Mapper.POS_MAPPER.get(e);
            VelocityComponent vel = Mapper.VEL_MAPPER.get(e);
            SpeedComponent mag = Mapper.SPEED_MAPPER.get(e);
            MovementDistanceComponent dist = Mapper.MOVE_DIST_MAPPER.get(e);

            Direction prevDir = lastDir.get(e);
            Vector2 target = targets.get(e);

            // entity is stopped during movement
            if (vel.dx == 0.f) {
                if (pos.x < target.x) vel.dx = mag.speed;
                else if (pos.x > target.x) vel.dx = -mag.speed;
            }
            if (vel.dy == 0.f) {
                if (pos.y < target.y) vel.dy = mag.speed;
                else if (pos.y > target.y) vel.dy = -mag.speed;
            }

            if ((prevDir == Direction.Up && pos.y >= target.y) ||
                    (prevDir == Direction.Right && pos.x >= target.x) ||
                    (prevDir == Direction.Down && pos.y <= target.y) ||
                    (prevDir == Direction.Left && pos.x <= target.x)) {
                pos.x = target.x;
                pos.y = target.y;
                vel.dx = vel.dy = 0.f;

                if (rotationDir.get(e)) {
                    switch (lastDir.get(e)) {
                        case Up:
                            vel.dx = mag.speed;
                            lastDir.put(e, Direction.Right);
                            targets.put(e, target.set(target.x + dist.dist, target.y));
                            break;
                        case Right:
                            vel.dy = -mag.speed;
                            lastDir.put(e, Direction.Down);
                            targets.put(e, target.set(target.x, target.y - dist.dist));
                            break;
                        case Down:
                            vel.dx = -mag.speed;
                            lastDir.put(e, Direction.Left);
                            targets.put(e, target.set(target.x - dist.dist, target.y));
                            break;
                        case Left:
                            vel.dy = mag.speed;
                            lastDir.put(e, Direction.Up);
                            targets.put(e, target.set(target.x, target.y + dist.dist));
                            break;
                    }
                } else {
                    switch (lastDir.get(e)) {
                        case Up:
                            vel.dx = -mag.speed;
                            lastDir.put(e, Direction.Left);
                            targets.put(e, target.set(target.x - dist.dist, target.y));
                            break;
                        case Left:
                            vel.dy = -mag.speed;
                            lastDir.put(e, Direction.Down);
                            targets.put(e, target.set(target.x, target.y - dist.dist));
                            break;
                        case Down:
                            vel.dx = mag.speed;
                            lastDir.put(e, Direction.Right);
                            targets.put(e, target.set(target.x + dist.dist, target.y));
                            break;
                        case Right:
                            vel.dy = mag.speed;
                            lastDir.put(e, Direction.Up);
                            targets.put(e, target.set(target.x, target.y + dist.dist));
                            break;
                    }
                }
            }
        }
    }

}
