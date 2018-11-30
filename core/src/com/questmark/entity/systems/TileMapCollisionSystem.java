package com.questmark.entity.systems;

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
public class TileMapCollisionSystem extends IteratingSystem {

    private QuadTree quadTree;
    private Array<Rectangle> collisions;

    // tile map bounding boxes
    private Array<Rectangle> boundingBoxes;
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
        BoundingBoxComponent boundingBox = Mapper.BOUNDING_BOX_MAPPER.get(entity);
        PositionComponent position = Mapper.POS_MAPPER.get(entity);
        VelocityComponent velocity = Mapper.VEL_MAPPER.get(entity);
        DimensionComponent size = Mapper.SIZE_MAPPER.get(entity);
        PreviousPositionComponent prevPosition = Mapper.PREV_POS_MAPPER.get(entity);
        boundingBox.bounds.setPosition(position.x + (size.width - boundingBox.bounds.width) / 2,
                position.y + (size.height - boundingBox.bounds.height) / 2);

        // check for going outside of map
        if (position.x < 0 || position.x > (mapWidth - 1) * tileSize
                || position.y < 0 || position.y > (mapHeight - 1) * tileSize)  {
            velocity.dx = 0.f;
            velocity.dy = 0.f;
            position.x = prevPosition.x;
            position.y = prevPosition.y;
        }

        collisions.clear();
        quadTree.retrieve(collisions, boundingBox.bounds);

        for (Rectangle bounds : collisions) {
            if (boundingBox.bounds.overlaps(bounds)) {
                velocity.dx = 0.f;
                velocity.dy = 0.f;
                position.x = prevPosition.x;
                position.y = prevPosition.y;
            }
        }
    }

    /**
     * Updates the collision system with map data.
     *
     * @param mapWidth
     * @param mapHeight
     * @param tileSize
     * @param boundingBoxes
     */
    public void setMapData(int mapWidth, int mapHeight, int tileSize, Array<Rectangle> boundingBoxes) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.tileSize = tileSize;
        this.boundingBoxes = boundingBoxes;

        quadTree = new QuadTree(0, new Rectangle(0, 0, mapWidth * tileSize, mapHeight * tileSize));
        // insert into quadtree only once in this instance because map bounding boxes don't change
        // currently. but if we decide map objects can move in the future (moving tiles) then it
        // will have to be inserted every update call
        for (Rectangle bounds : boundingBoxes) {
            quadTree.insert(bounds);
        }
    }

}
