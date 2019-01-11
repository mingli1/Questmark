package com.questmark.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.PlayerComponent;
import com.questmark.entity.components.SpeedComponent;
import com.questmark.entity.components.VelocityComponent;
import com.questmark.input.Direction;
import com.questmark.input.KeyInputHandler;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the updating of the player entity's
 * position based on keyboard input events.
 *
 * @author Ming Li
 */
public class KeyInputSystem extends EntitySystem implements KeyInputHandler {

    private Entity player;

    @Override
    public void addedToEngine(Engine engine) {
        player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).get(0);
    }

    @Override
    public void move(Direction direction) {
        VelocityComponent velocity = Mapper.VEL_MAPPER.get(player);
        SpeedComponent speed = Mapper.SPEED_MAPPER.get(player);
        velocity.move(direction, speed.speed);
    }

    @Override
    public void stop(Direction direction) {
        VelocityComponent velocity = Mapper.VEL_MAPPER.get(player);
        switch (direction) {
            case Up:
                if (velocity.v.y > 0) velocity.v.y = 0.f;
                break;
            case Down:
                if (velocity.v.y < 0) velocity.v.y = 0.f;
                break;
            case Left:
                if (velocity.v.x < 0) velocity.v.x = 0.f;
                break;
            case Right:
                if (velocity.v.x > 0) velocity.v.x = 0.f;
                break;
        }
    }

}
