package com.questmark.entity.systems.enemy;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.*;
import com.questmark.entity.components.enemy.AggressionComponent;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.enemy.MovementFrequencyComponent;
import com.questmark.entity.components.enemy.SourcePositionComponent;
import com.questmark.entity.systems.collision.CollisionSystem;
import com.questmark.pathfinding.AStar;
import com.questmark.pathfinding.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the movement of enemies. In this system,
 * enemies follows the player using a path calculated by the A* search algorithm.
 *
 * @author Ming Li
 */
public class AStarMovementSystem extends IteratingSystem implements CollisionSystem {

    private AStar alg;
    private ImmutableArray<Entity> collidableEntities;
    private Array<Rectangle> mapCollisions;
    private Array<Rectangle> allCollisions;
    private int tileSize;

    private Entity player;
    private Map<Entity, Array<Node>> paths;
    private Map<Entity, Float> timers;
    private Map<Entity, Boolean> toSource;
    private Array<Node> returnPath;

    public AStarMovementSystem() {
        super(Family.all(EnemyComponent.class, AggressionComponent.class, SourcePositionComponent.class,
                MovementFrequencyComponent.class).get());
        paths = new HashMap<Entity, Array<Node>>();
        timers = new HashMap<Entity, Float>();
        toSource = new HashMap<Entity, Boolean>();
        allCollisions = new Array<Rectangle>();
        returnPath = new Array<Node>();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        for (Entity e : this.getEntities()) {
            timers.put(e, 0.f);
            toSource.put(e, false);
        }
        player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).get(0);
        collidableEntities = engine.getEntitiesFor(Family.all(BoundingBoxComponent.class)
                .exclude(EnemyComponent.class, PlayerComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AggressionComponent agg = Mapper.AGGRESSION_MAPPER.get(entity);
        PositionComponent pos = Mapper.POS_MAPPER.get(entity);
        PositionComponent playerPos = Mapper.POS_MAPPER.get(player);
        VelocityComponent vel = Mapper.VEL_MAPPER.get(entity);
        SpeedComponent mag = Mapper.SPEED_MAPPER.get(entity);

        // player within aggression range
        if (pos.p.dst(playerPos.p) <= agg.range) {
            toSource.put(entity, false);
            timers.put(entity, timers.get(entity) + deltaTime);

            MovementFrequencyComponent freq = Mapper.MOVE_FREQ_MAPPER.get(entity);

            if (timers.get(entity) > freq.frequency) {
                Vector2 source = new Vector2(Math.round(pos.p.x / tileSize) * tileSize,
                        Math.round(pos.p.y / tileSize) * tileSize);
                Vector2 target = new Vector2(Math.round(playerPos.p.x / tileSize) * tileSize,
                        Math.round(playerPos.p.y / tileSize) * tileSize);

                paths.put(entity, getPath(entity, source, target));
                timers.put(entity, timers.get(entity) - freq.frequency);
            }

            Array<Node> path = paths.get(entity);
            if (path != null) {
                if (path.size > 0) {
                    Vector2 target = path.get(path.size - 1).position;
                    move(pos.p, target, vel.v, mag.speed, deltaTime);
                }
            }
        }
        else {
            if (!toSource.get(entity)) {
                SourcePositionComponent source = Mapper.SOURCE_POS_MAPPER.get(entity);
                returnPath.clear();
                returnPath = getPath(entity, new Vector2(Math.round(pos.p.x / tileSize) * tileSize,
                        Math.round(pos.p.y / tileSize) * tileSize), source.s);
                toSource.put(entity, true);
            }

            if (returnPath.size > 0) {
                Vector2 target = returnPath.get(returnPath.size - 1).position;
                move(pos.p, target, vel.v, mag.speed, deltaTime);
                if (pos.p.equals(target)) {
                    returnPath.removeIndex(returnPath.size - 1);
                }
            }
        }
    }

    @Override
    public void setMapData(int mapWidth, int mapHeight, int tileSize, Array<Rectangle> boundingBoxes) {
        alg = new AStar(mapWidth, mapHeight, tileSize);
        this.mapCollisions = boundingBoxes;
        this.tileSize = tileSize;
    }

    /**
     * Runs the A* algorithm with a given source and target on a given entity.
     * Helper method that does the initial collision data setup.
     *
     * @param e
     * @param source
     * @param target
     * @return
     */
    private Array<Node> getPath(Entity e, Vector2 source, Vector2 target) {
        allCollisions.clear();
        allCollisions.addAll(mapCollisions);
        for (Entity entity : collidableEntities) {
            if (!e.equals(entity)) {
                BoundingBoxComponent bb = Mapper.BOUNDING_BOX_MAPPER.get(e);
                allCollisions.add(bb.bounds);
            }
        }
        alg.setCollisionData(allCollisions);
        return alg.findPath(source, target);
    }

    private void move(Vector2 source, Vector2 target, Vector2 vel, float speed, float dt) {
        if (source.x < target.x) {
            if (source.x + speed * dt > target.x) {
                vel.x = 0.f;
                source.x = target.x;
            } else vel.x = speed;
        }
        if (source.x > target.x) {
            if (source.x - speed * dt < target.x) {
                vel.x = 0.f;
                source.x = target.x;
            } else vel.x = -speed;
        }
        if (source.y < target.y) {
            if (source.y + speed * dt > target.y) {
                vel.y = 0.f;
                source.y = target.y;
            } else vel.y = speed;
        }
        if (source.y > target.y) {
            if (source.y - speed * dt < target.y) {
                vel.y = 0.f;
                source.y = target.y;
            } else vel.y = -speed;
        }
    }

}
