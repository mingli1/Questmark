package com.questmark.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.PositionComponent;
import com.questmark.entity.components.TextureComponent;

/**
 * A LibGDX Ashley {@link EntitySystem} that handles the rendering of entities
 * based on texture and position components.
 *
 * @author Ming Li
 */
public class RenderSystem extends IteratingSystem {

    private Batch batch;

    public RenderSystem(Batch batch) {
        super(Family.all(TextureComponent.class, PositionComponent.class).get());
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float dt) {
        TextureComponent texture = Mapper.INSTANCE.getTEXTURE_MAPPER().get(entity);
        PositionComponent position = Mapper.INSTANCE.getPOS_MAPPER().get(entity);

        batch.draw(texture.texture, position.p.x, position.p.y);
    }

}
