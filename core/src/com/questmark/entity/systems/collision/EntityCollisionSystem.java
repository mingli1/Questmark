package com.questmark.entity.systems.collision;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.questmark.entity.Mapper;
import com.questmark.entity.QuadTree;
import com.questmark.entity.components.*;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the collision between any two entities
 * via their {@link com.questmark.entity.components.BoundingBoxComponent} using axis aligned calculations.
 *
 * @author Ming Li
 */
public class EntityCollisionSystem extends EntitySystem implements CollisionSystem {

    private QuadTree quadTree;
    private Array<Rectangle> collisions;

    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(BoundingBoxComponent.class, PositionComponent.class,
                VelocityComponent.class, PreviousPositionComponent.class).get());
        collisions = new Array<Rectangle>();
    }

    @Override
    public void update(float dt) {
        quadTree.clear();
        for (Entity e : entities) {
            BoundingBoxComponent b = Mapper.BOUNDING_BOX_MAPPER.get(e);
            quadTree.insert(b.bounds);
        }

        for (Entity e : entities) {
            BoundingBoxComponent bb = Mapper.BOUNDING_BOX_MAPPER.get(e);
            PositionComponent p = Mapper.POS_MAPPER.get(e);
            VelocityComponent v = Mapper.VEL_MAPPER.get(e);
            PreviousPositionComponent pp = Mapper.PREV_POS_MAPPER.get(e);

            collisions.clear();
            quadTree.retrieve(collisions, bb.bounds);

            for (Rectangle bounds : collisions) {
                if (!bb.bounds.equals(bounds)) {
                    if (bb.bounds.overlaps(bounds)) {
                        v.dx = v.dy = 0.f;
                        p.x = pp.x;
                        p.y = pp.y;
                    }
                }
            }
        }
    }

    @Override
    public void setMapData(int mapWidth, int mapHeight, int tileSize, Array<Rectangle> boundingBoxes) {
        quadTree = new QuadTree(0, new Rectangle(0, 0, mapWidth * tileSize, mapHeight * tileSize));
    }

}
