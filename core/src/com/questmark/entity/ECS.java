package com.questmark.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.questmark.entity.entities.Player;
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
    private CollisionSystem collisionSystem;

    // entities
    private Player player;

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
        collisionSystem = new CollisionSystem();

        engine.addSystem(movementSystem);
        engine.addSystem(collisionSystem);
        engine.addSystem(renderSystem);
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

}
