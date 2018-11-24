package com.questmark.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.questmark.entity.entities.Player;
import com.questmark.entity.entities.Enemy;
import com.questmark.entity.systems.*;
import com.questmark.util.Resources;

/**
 * The manager for the Ashley entity component system (ECS).
 * Contains the universal {@link Engine} for all Ashley {@link Entity} components and systems.
 *
 * @author Ming Li
 */
public final class ECS implements Disposable {

    private Engine engine;

    // default systems that are inherent with entites
    private MovementSystem movementSystem;
    private RenderSystem renderSystem;
    private TileMapCollisionSystem tileMapCollisionSystem;
    private EntityCollisionSystem entityCollisionSystem;
    private EnemyMovementSystem enemyMovementSystem;

    // entities
    private Player player;
    private Enemy enemy;
    private Enemy enemy2;

    public ECS(Batch batch, Resources res) {
        engine = new Engine();

        this.addEntites(res);
        this.addSystems(batch, res);
    }

    /**
     * Initializes and adds all entities to the engine.
     */
    private void addEntites(Resources res) {
        player = new Player(new Vector2(0, 0), res);
        enemy = new Enemy(new Vector2(100, 100), res);
        enemy2 = new Enemy(new Vector2(140, 100), res);

        engine.addEntity(player);
        engine.addEntity(enemy);
        engine.addEntity(enemy2);
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
        enemyMovementSystem = new EnemyMovementSystem();

        addSystem(movementSystem);
        addSystem(tileMapCollisionSystem);
        addSystem(entityCollisionSystem);
        addSystem(enemyMovementSystem);
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
     * Returns the universal player instance.
     *
     * @return
     */
    public Player getPlayer() {
        return player;
    }

}
