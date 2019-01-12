package com.questmark.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.PositionComponent;
import com.questmark.entity.components.PreviousPositionComponent;
import com.questmark.entity.components.VelocityComponent;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the updating of position components
 * based on velocity components.
 *
 * @author Ming Li
 */
public class MovementSystem extends IteratingSystem {

    public MovementSystem() {
        super(Family.all(PositionComponent.class, VelocityComponent.class, PreviousPositionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        PositionComponent position = Mapper.INSTANCE.getPOS_MAPPER().get(entity);
        VelocityComponent velocity = Mapper.INSTANCE.getVEL_MAPPER().get(entity);
        PreviousPositionComponent prevPosition = Mapper.INSTANCE.getPREV_POS_MAPPER().get(entity);

        prevPosition.p.set(position.p);
        position.p.x += velocity.v.x * dt;
        position.p.y += velocity.v.y * dt;
    }

}
