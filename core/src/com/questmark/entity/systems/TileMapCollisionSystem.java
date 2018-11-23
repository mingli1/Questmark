package com.questmark.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.BoundingBoxComponent;
import com.questmark.entity.components.PositionComponent;
import com.questmark.entity.components.PreviousPositionComponent;
import com.questmark.entity.components.VelocityComponent;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the collision between entities
 * and the tile map via their {@link BoundingBoxComponent} using axis aligned calculations.
 *
 * @author Ming Li
 */
public class TileMapCollisionSystem extends IteratingSystem {

    // tile map bounding boxes
    private Array<Rectangle> boundingBoxes;

    public TileMapCollisionSystem() {
        super(Family.all(BoundingBoxComponent.class, PositionComponent.class,
                VelocityComponent.class, PreviousPositionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        BoundingBoxComponent boundingBox = Mapper.BOUNDING_BOX_MAPPER.get(entity);
        PositionComponent position = Mapper.POS_MAPPER.get(entity);
        VelocityComponent velocity = Mapper.VEL_MAPPER.get(entity);
        PreviousPositionComponent prevPosition = Mapper.PREV_POS_MAPPER.get(entity);
        boundingBox.bounds.setPosition(position.getPos());

        for (Rectangle mapBounds : boundingBoxes) {
            if (boundingBox.bounds.overlaps(mapBounds)) {
                velocity.dx = 0.f;
                velocity.dy = 0.f;
                position.x = prevPosition.x;
                position.y = prevPosition.y;
            }
        }
    }

    /**
     * Updates the list of bounding boxes for the tiled map for each new map loading.
     *
     * @param boundingBoxes
     */
    public void setBoundingBoxes(Array<Rectangle> boundingBoxes) {
        this.boundingBoxes = boundingBoxes;
    }

}
