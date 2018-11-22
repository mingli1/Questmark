package com.questmark.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
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
public class RenderSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;
    private Batch batch;

    public RenderSystem(Batch batch) {
        this.batch = batch;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(TextureComponent.class, PositionComponent.class).get());
    }

    @Override
    /**
     * This should be surrounded by SpriteBatch.begin and SpriteBatch.end
     * Will probably rewrite this in the future
     */
    public void update(float dt) {
        for (Entity e : entities) {
            TextureComponent texture = Mapper.TEXTURE_MAPPER.get(e);
            PositionComponent position = Mapper.POS_MAPPER.get(e);

            batch.draw(texture.texture, position.x, position.y);
        }
    }

    @Override
    public String toString() {
        return "render";
    }

}
