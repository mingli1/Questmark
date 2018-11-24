package com.questmark.entity.systems.enemy;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.SpeedComponent;
import com.questmark.entity.components.VelocityComponent;
import com.questmark.entity.components.enemy.CircularMovementComponent;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.enemy.MovementFrequencyComponent;
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

    private Map<Entity, Float> timers;

    private ImmutableArray<Entity> entities;
    // map of entity to its rotation scheme (true for clockwise, false for counterclockwise)
    private Map<Entity, Boolean> rotationDir;
    private Map<Entity, Direction> lastDir;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(EnemyComponent.class, CircularMovementComponent.class,
                MovementFrequencyComponent.class).get());
        timers = new HashMap<Entity, Float>();
        rotationDir = new HashMap<Entity, Boolean>();
        lastDir = new HashMap<Entity, Direction>();

        for (Entity e : entities) {
            timers.put(e, 0.f);
            rotationDir.put(e, MathUtils.randomBoolean());
            lastDir.put(e, Direction.Up);
        }
    }

    @Override
    public void update(float dt) {
        for (Entity e : entities) {
            timers.put(e, timers.get(e) + dt);

            VelocityComponent vel = Mapper.VEL_MAPPER.get(e);
            SpeedComponent mag = Mapper.SPEED_MAPPER.get(e);
            MovementFrequencyComponent freq = Mapper.MOVE_FREQ_MAPPER.get(e);

            if (timers.get(e) > freq.frequency) {
                vel.dx = vel.dy = 0.f;
                if (rotationDir.get(e)) {
                    switch (lastDir.get(e)) {
                        case Up:
                            vel.dx = mag.speed;
                            lastDir.put(e, Direction.Right);
                            break;
                        case Right:
                            vel.dy = -mag.speed;
                            lastDir.put(e, Direction.Down);
                            break;
                        case Down:
                            vel.dx = -mag.speed;
                            lastDir.put(e, Direction.Left);
                            break;
                        case Left:
                            vel.dy = mag.speed;
                            lastDir.put(e, Direction.Up);
                            break;
                    }
                }
                else {
                    switch (lastDir.get(e)) {
                        case Up:
                            vel.dx = -mag.speed;
                            lastDir.put(e, Direction.Left);
                            break;
                        case Left:
                            vel.dy = -mag.speed;
                            lastDir.put(e, Direction.Down);
                            break;
                        case Down:
                            vel.dx = mag.speed;
                            lastDir.put(e, Direction.Right);
                            break;
                        case Right:
                            vel.dy = mag.speed;
                            lastDir.put(e, Direction.Up);
                            break;
                    }
                }
                timers.put(e, timers.get(e) - freq.frequency);
            }
        }
    }

}
