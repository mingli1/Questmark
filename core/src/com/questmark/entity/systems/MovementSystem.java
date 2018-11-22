package com.questmark.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.PositionComponent;
import com.questmark.entity.components.VelocityComponent;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the updating of position components
 * based on velocity components.
 *
 * @author Ming Li
 */
public class MovementSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    @Override
    public void update(float dt) {
        for (Entity e : entities) {
            PositionComponent position = Mapper.POS_MAPPER.get(e);
            VelocityComponent velocity = Mapper.VEL_MAPPER.get(e);

            position.x += velocity.dx * dt;
            position.y += velocity.dy * dt;
        }
    }

    @Override
    public String toString() {
        return "movement";
    }

}
