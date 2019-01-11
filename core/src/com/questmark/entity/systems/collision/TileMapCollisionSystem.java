package com.questmark.entity.systems.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.questmark.entity.Mapper;
import com.questmark.entity.QuadTree;
import com.questmark.entity.components.*;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the collision between entities
 * and the tile map via their {@link BoundingBoxComponent} using axis aligned calculations.
 *
 * @author Ming Li
 */
public class TileMapCollisionSystem extends IteratingSystem implements CollisionSystem {

    private QuadTree quadTree;
    private Array<Rectangle> collisions;

    // tile map bounding boxes
    private int mapWidth;
    private int mapHeight;
    private int tileSize;

    public TileMapCollisionSystem() {
        super(Family.all(BoundingBoxComponent.class, PositionComponent.class,
                VelocityComponent.class, PreviousPositionComponent.class).get());
        collisions = new Array<Rectangle>();
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        BoundingBoxComponent bb = Mapper.BOUNDING_BOX_MAPPER.get(entity);
        PositionComponent position = Mapper.POS_MAPPER.get(entity);
        VelocityComponent velocity = Mapper.VEL_MAPPER.get(entity);
        DimensionComponent size = Mapper.SIZE_MAPPER.get(entity);
        PreviousPositionComponent prevPosition = Mapper.PREV_POS_MAPPER.get(entity);
        bb.bounds.setPosition(position.p.x + (size.width - bb.bounds.width) / 2,
                position.p.y + (size.height - bb.bounds.height) / 2);

        // check for going outside of map
        if (position.p.x < 0 || position.p.x > (mapWidth - 1) * tileSize) {
            position.p.x = prevPosition.p.x;
        }
        if (position.p.y < 0 || position.p.y > (mapHeight - 1) * tileSize) {
            position.p.y = prevPosition.p.y;
        }

        collisions.clear();
        quadTree.retrieve(collisions, bb.bounds);

        for (Rectangle bounds : collisions) {
            if (bb.bounds.overlaps(bounds)) {
                float bx = prevPosition.p.x + (size.width - bb.bounds.width) / 2;
                float by = prevPosition.p.y + (size.height - bb.bounds.height) / 2;
                if (bx + bb.bounds.width > bounds.x && bx <= bounds.x + bounds.width && velocity.v.y != 0) {
                    position.p.y = prevPosition.p.y;
                }
                else if (by + bb.bounds.height > bounds.y && by <= bounds.y + bounds.height && velocity.v.x != 0) {
                    position.p.x = prevPosition.p.x;
                }
            }
        }
    }

    @Override
    public void setMapData(int mapWidth, int mapHeight, int tileSize, Array<Rectangle> boundingBoxes) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.tileSize = tileSize;

        quadTree = new QuadTree(0, new Rectangle(0, 0, mapWidth * tileSize, mapHeight * tileSize));
        // insert into quadtree only once in this instance because map bounding boxes don't change
        // currently. but if we decide map objects can move in the future (moving tiles) then it
        // will have to be inserted every update call
        for (Rectangle bounds : boundingBoxes) {
            quadTree.insert(bounds);
        }
    }

}
