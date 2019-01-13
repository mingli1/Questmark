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
import com.questmark.entity.components.enemy.SourcePositionComponent;
import com.questmark.entity.systems.MapSystem;
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
public class AStarMovementSystem extends IteratingSystem implements MapSystem {

    private AStar alg;
    private ImmutableArray<Entity> collidableEntities;
    private Array<Rectangle> mapCollisions;
    private Array<Rectangle> allCollisions;
    private int tileSize;

    private Entity player;
    private Map<Entity, Array<Node>> paths;
    private Map<Entity, Float> timers;
    private Map<Entity, Float> freqs;
    private Map<Entity, Boolean> toSource;
    private Map<Entity, Array<Node>> returnPaths;

    public AStarMovementSystem() {
        super(Family.all(EnemyComponent.class, AggressionComponent.class, SourcePositionComponent.class).get());
        paths = new HashMap<Entity, Array<Node>>();
        timers = new HashMap<Entity, Float>();
        freqs = new HashMap<Entity, Float>();
        toSource = new HashMap<Entity, Boolean>();
        allCollisions = new Array<Rectangle>();
        returnPaths = new HashMap<Entity, Array<Node>>();
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
        AggressionComponent agg = Mapper.INSTANCE.getAGGRESSION_MAPPER().get(entity);
        PositionComponent pos = Mapper.INSTANCE.getPOS_MAPPER().get(entity);
        PositionComponent playerPos = Mapper.INSTANCE.getPOS_MAPPER().get(player);
        VelocityComponent vel = Mapper.INSTANCE.getVEL_MAPPER().get(entity);
        SpeedComponent mag = Mapper.INSTANCE.getSPEED_MAPPER().get(entity);

        // player within aggression range
        if (pos.getP().dst(playerPos.getP()) <= agg.getRange() || agg.getRange() == -1.f) {
            agg.setAtSource(false);
            toSource.put(entity, false);
            timers.put(entity, timers.get(entity) + deltaTime);

            if (timers.get(entity) > freqs.get(entity)) {
                Vector2 source = new Vector2(Math.round(pos.getP().x / tileSize) * tileSize,
                        Math.round(pos.getP().y / tileSize) * tileSize);
                Vector2 target = new Vector2(Math.round(playerPos.getP().x / tileSize) * tileSize,
                        Math.round(playerPos.getP().y / tileSize) * tileSize);
                if (source.equals(target)) vel.getV().set(0.f, 0.f);
                else paths.put(entity, getPath(entity, source, target));
                timers.put(entity, timers.get(entity) - freqs.get(entity));
            }

            Array<Node> path = paths.get(entity);
            if (path != null) {
                if (path.size > 0) {
                    Vector2 target = path.get(path.size - 1).position;
                    move(pos.getP(), target, vel.getV(), mag.getSpeed(), deltaTime);
                }
            }
        }
        else {
            SourcePositionComponent source = Mapper.INSTANCE.getSOURCE_POS_MAPPER().get(entity);
            int sx = ((int) source.getS().x / tileSize) * tileSize;
            int sy = ((int) source.getS().y / tileSize) * tileSize;

            if (!toSource.get(entity)) {
                Vector2 s = new Vector2(sx, sy);
                returnPaths.put(entity, getPath(entity, new Vector2(Math.round(pos.getP().x / tileSize) * tileSize,
                        Math.round(pos.getP().y / tileSize) * tileSize), s));
                toSource.put(entity, true);
            }

            Array<Node> returnPath = returnPaths.get(entity);
            if (returnPath != null) {
                if (returnPath.size > 0) {
                    Vector2 target = returnPath.get(returnPath.size - 1).position;
                    if (sx == target.x && sy == target.y) {
                        move(pos.getP(), source.getS(), vel.getV(), mag.getSpeed(), deltaTime);
                        if (pos.getP().equals(source.getS())) returnPath.removeIndex(returnPath.size - 1);
                    } else {
                        move(pos.getP(), target, vel.getV(), mag.getSpeed(), deltaTime);
                        if (pos.getP().equals(target)) returnPath.removeIndex(returnPath.size - 1);
                    }
                }
            }
            if (pos.getP().equals(source.getS())) agg.setAtSource(true);
        }
    }

    @Override
    public void setMapData(int mapWidth, int mapHeight, int tileSize, Array<Rectangle> boundingBoxes) {
        this.alg = new AStar(mapWidth, mapHeight, tileSize);
        this.mapCollisions = boundingBoxes;
        this.tileSize = tileSize;
        for (Entity e : this.getEntities()) {
            SpeedComponent mag = Mapper.INSTANCE.getSPEED_MAPPER().get(e);
            freqs.put(e, (tileSize / 2) / mag.getSpeed());
        }
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
                BoundingBoxComponent bb = Mapper.INSTANCE.getBOUNDING_BOX_MAPPER().get(e);
                allCollisions.add(bb.getBounds());
            }
        }
        alg.setCollisionData(allCollisions);
        return alg.findPath(source, target);
    }

    /**
     * Moves an entity from a source to a target by updating its velocity.
     *
     * @param source
     * @param target
     * @param vel
     * @param speed
     * @param dt
     */
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
