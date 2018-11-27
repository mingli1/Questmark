package com.questmark.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.*;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the collision between any two entities
 * via their {@link com.questmark.entity.components.BoundingBoxComponent} using axis aligned calculations.
 *
 * @author Ming Li
 */
public class EntityCollisionSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;
    private Rectangle bound1;
    private Rectangle bound2;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(BoundingBoxComponent.class, PositionComponent.class,
                VelocityComponent.class, PreviousPositionComponent.class).get());
        bound1 = new Rectangle();
        bound2 = new Rectangle();
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
                    DimensionComponent d1 = Mapper.SIZE_MAPPER.get(e1);
                    PreviousPositionComponent pp1 = Mapper.PREV_POS_MAPPER.get(e1);

                    BoundingBoxComponent bb2 = Mapper.BOUNDING_BOX_MAPPER.get(e2);
                    PositionComponent p2 = Mapper.POS_MAPPER.get(e2);
                    VelocityComponent v2 = Mapper.VEL_MAPPER.get(e2);
                    DimensionComponent d2 = Mapper.SIZE_MAPPER.get(e2);
                    PreviousPositionComponent pp2 = Mapper.PREV_POS_MAPPER.get(e2);

                    bound1.set(p1.x + (d1.width - (bb1.bounds.width / 2)) / 2,
                            p1.y + (d1.height - (bb1.bounds.height / 2)) / 2,
                            bb1.bounds.width / 2, bb1.bounds.height / 2);

                    bound2.set(p2.x + (d2.width - (bb2.bounds.width / 2)) / 2,
                            p2.y + (d2.height - (bb2.bounds.height / 2)) / 2,
                            bb2.bounds.width / 2, bb2.bounds.height / 2);

                    if (bound1.overlaps(bound2)) {
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
