package com.questmark.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.BoundingBoxComponent;
import com.questmark.entity.components.PositionComponent;
import com.questmark.entity.components.PreviousPositionComponent;
import com.questmark.entity.components.VelocityComponent;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the collision between any two entities
 * via their {@link com.questmark.entity.components.BoundingBoxComponent} using axis aligned calculations.
 *
 * @author Ming Li
 */
public class EntityCollisionSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(BoundingBoxComponent.class, PositionComponent.class,
                VelocityComponent.class, PreviousPositionComponent.class).get());
    }

    @Override
    public void update(float dt) {
        for (int i = 0; i < entities.size(); i++) {
            Entity e1 = entities.get(i);
            for (int j = 0; j < entities.size(); j++) {
                Entity e2 = entities.get(j);
                if (!e1.equals(e2)) {
                    BoundingBoxComponent bb1 = Mapper.BOUNDING_BOX_MAPPER.get(e1);
                    PositionComponent p1 = Mapper.POS_MAPPER.get(e1);
                    VelocityComponent v1 = Mapper.VEL_MAPPER.get(e1);
                    PreviousPositionComponent pp1 = Mapper.PREV_POS_MAPPER.get(e1);

                    BoundingBoxComponent bb2 = Mapper.BOUNDING_BOX_MAPPER.get(e2);
                    PositionComponent p2 = Mapper.POS_MAPPER.get(e2);
                    VelocityComponent v2 = Mapper.VEL_MAPPER.get(e2);
                    PreviousPositionComponent pp2 = Mapper.PREV_POS_MAPPER.get(e2);

                    if (bb1.bounds.overlaps(bb2.bounds)) {
                        v1.dx = v2.dx = v1.dy = v2.dy = 0.f;
                        p1.x = pp1.x;
                        p1.y = pp1.y;
                        p2.x = pp2.x;
                        p2.y = pp2.y;
                    }
                }
            }
        }
    }

}
