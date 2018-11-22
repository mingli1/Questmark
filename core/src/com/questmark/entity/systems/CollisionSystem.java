package com.questmark.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.questmark.entity.components.BoundingBoxComponent;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the collision between entities
 * via their {@link BoundingBoxComponent} using axis aligned calculations.
 *
 * @author Ming Li
 */
public class CollisionSystem extends EntitySystem {

    private Entity player;

    /**
     * In here AABB collision detection will be implemented to check collision between the player
     * and other collidable entities. Eg. Player and {@link ImmutableArray} of monster entities.
     * The system should then send a message to a event manager class to process the collision.
     */

    @Override
    public String toString() {
        return "collision";
    }

}
