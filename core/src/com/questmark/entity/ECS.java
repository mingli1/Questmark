package com.questmark.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.questmark.entity.entities.Player;
import com.questmark.entity.systems.*;
import com.questmark.entity.systems.collision.*;
import com.questmark.entity.systems.enemy.*;
import com.questmark.util.Resources;

/**
 * The manager for the Ashley entity component system (ECS).
 * Contains the universal {@link Engine} for all Ashley {@link Entity} components and systems.
 *
 * @author Ming Li
 */
public final class ECS implements Disposable {

    private Engine engine;

    // default systems that are inherent with entities
    private MovementSystem movementSystem;
    private RenderSystem renderSystem;
    private TileMapCollisionSystem tileMapCollisionSystem;
    private EntityCollisionSystem entityCollisionSystem;
    private RandomMovementSystem randomMovementSystem;
    private CircularMovementSystem circularMovementSystem;
    private HorizontalMovementSystem horizontalMovementSystem;
    private VerticalMovementSystem verticalMovementSystem;
    private AStarMovementSystem aStarMovementSystem;

    // entities
    private Player player;

    public ECS(Batch batch, Resources res) {
        engine = new Engine();

        this.addEntities(res);
        this.addSystems(batch, res);
    }

    /**
     * Initializes and adds all entities to the engine.
     */
    private void addEntities(Resources res) {
        player = new Player(new Vector2(168, 180), res);

        engine.addEntity(player);
    }

    /**
     * Initializes and adds all systems to the engine in priority order.
     *
     * @param batch
     * @param res
     */
    private void addSystems(Batch batch, Resources res) {
        movementSystem = new MovementSystem();
        renderSystem = new RenderSystem(batch);
        tileMapCollisionSystem = new TileMapCollisionSystem();
        entityCollisionSystem = new EntityCollisionSystem();
        randomMovementSystem = new RandomMovementSystem();
        circularMovementSystem = new CircularMovementSystem();
        horizontalMovementSystem = new HorizontalMovementSystem();
        verticalMovementSystem = new VerticalMovementSystem();
        aStarMovementSystem = new AStarMovementSystem();

        addSystem(movementSystem);
        addSystem(tileMapCollisionSystem);
        addSystem(entityCollisionSystem);
        addSystem(randomMovementSystem);
        addSystem(circularMovementSystem);
        addSystem(horizontalMovementSystem);
        addSystem(verticalMovementSystem);
        addSystem(aStarMovementSystem);
        addSystem(renderSystem);
    }

    public void update(float dt) {
        engine.update(dt);
    }

    @Override
    public void dispose() {

    }

    /**
     * Adds an {@link EntitySystem} to the engine.
     *
     * @param system
     */
    public void addSystem(EntitySystem system) {
        engine.addSystem(system);
    }

    /**
     * Returns an {@link EntitySystem} from a given {@link com.badlogic.ashley.core.Component} class type.
     *
     * @param system
     */
    public <T extends EntitySystem> T getSystem(Class<T> system) {
        return engine.getSystem(system);
    }

    /**
     * Toggles a given {@link EntitySystem} on or off.
     *
     * @param system the given entity system
     * @param toggle toggle on or off
     * @param <T>
     */
    public <T extends EntitySystem> void toggleProcessing(Class<T> system, boolean toggle) {
        engine.getSystem(system).setProcessing(toggle);
    }

    /**
     * Returns the universal player instance.
     *
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Updates {@link com.questmark.entity.systems.collision.CollisionSystem} systems with tile map data.
     *
     * @param mapWidth
     * @param mapHeight
     * @param tileSize
     * @param boundingBoxes
     */
    public void updateCollisionSystems(int mapWidth, int mapHeight, int tileSize, Array<Rectangle> boundingBoxes) {
        tileMapCollisionSystem.setMapData(mapWidth, mapHeight, tileSize, boundingBoxes);
        entityCollisionSystem.setMapData(mapWidth, mapHeight, tileSize, boundingBoxes);
        aStarMovementSystem.setMapData(mapWidth, mapHeight, tileSize, boundingBoxes);
    }

}
