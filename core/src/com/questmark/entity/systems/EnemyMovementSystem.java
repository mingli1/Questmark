package com.questmark.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.EnemyComponent;
import com.questmark.entity.components.VelocityComponent;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the movement of enemies. Enemies
 * can have different movement patterns and even follow the player using A* pathfinding.
 *
 * @author Ming Li
 */
public class EnemyMovementSystem extends IteratingSystem {

    private int time = 0;

    public EnemyMovementSystem() {
        super(Family.all(EnemyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        
    }

}
