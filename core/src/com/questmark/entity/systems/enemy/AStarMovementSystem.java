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
import com.questmark.entity.components.enemy.AStarMovementComponent;
import com.questmark.entity.components.enemy.EnemyComponent;
import com.questmark.entity.components.enemy.MovementFrequencyComponent;
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
    private Rectangle currBounds;
    private int tileSize;

    private Entity player;
    private Map<Entity, Array<Node>> paths;
    private Map<Entity, Float> timers;

    public AStarMovementSystem() {
        super(Family.all(EnemyComponent.class, AStarMovementComponent.class, MovementFrequencyComponent.class).get());
        paths = new HashMap<Entity, Array<Node>>();
        timers = new HashMap<Entity, Float>();
        allCollisions = new Array<Rectangle>();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        for (Entity e : this.getEntities()) {
            timers.put(e, 0.f);
        }
        player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).get(0);
        collidableEntities = engine.getEntitiesFor(Family.all(BoundingBoxComponent.class)
                .exclude(EnemyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        timers.put(entity, timers.get(entity) + deltaTime);

        PositionComponent pos = Mapper.POS_MAPPER.get(entity);
        VelocityComponent vel = Mapper.VEL_MAPPER.get(entity);
        SpeedComponent mag = Mapper.SPEED_MAPPER.get(entity);
        PositionComponent playerPos = Mapper.POS_MAPPER.get(player);
        MovementFrequencyComponent freq = Mapper.MOVE_FREQ_MAPPER.get(entity);

        if (timers.get(entity) > freq.frequency) {
            allCollisions.clear();
            allCollisions.addAll(mapCollisions);
            for (Entity e : collidableEntities) {
                if (!entity.equals(e) && !e.equals(player)) {
                    BoundingBoxComponent bb = Mapper.BOUNDING_BOX_MAPPER.get(e);
                    allCollisions.add(bb.bounds);
                }
            }
            currBounds.setPosition(((int) pos.getPos().x / tileSize) * tileSize,
                    ((int) pos.getPos().y / tileSize) * tileSize);
            boolean shouldCalculate = true;
            for (Rectangle bounds : allCollisions) {
                if (bounds.overlaps(currBounds)) {
                    shouldCalculate = false;
                    break;
                }
            }

            if (shouldCalculate) {
                alg.setCollisionData(allCollisions);
                Vector2 source = new Vector2(((int) pos.getPos().x / tileSize) * tileSize,
                        ((int) pos.getPos().y / tileSize) * tileSize);
                Vector2 target = new Vector2(((int) playerPos.getPos().x / tileSize) * tileSize,
                        ((int) playerPos.getPos().y / tileSize) * tileSize);
                if (source.equals(target)) return;

                paths.put(entity, alg.findPath(source, target));
            }
            timers.put(entity, timers.get(entity) - freq.frequency);
        }

        Array<Node> path = paths.get(entity);
        if (path != null) {
            if (path.size > 0) {
                Vector2 target = path.get(path.size - 1).position;
                if (pos.x < target.x) vel.dx = mag.speed;
                if (pos.x > target.x) vel.dx = -mag.speed;
                if (pos.y < target.y) vel.dy = mag.speed;
                if (pos.y > target.y) vel.dy = -mag.speed;
            }
        }
    }

    @Override
    public void setMapData(int mapWidth, int mapHeight, int tileSize, Array<Rectangle> boundingBoxes) {
        alg = new AStar(mapWidth, mapHeight, tileSize);
        this.mapCollisions = boundingBoxes;
        this.tileSize = tileSize;
        currBounds = new Rectangle(0, 0, tileSize, tileSize);
    }

}
