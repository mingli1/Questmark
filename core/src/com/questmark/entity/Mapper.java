package com.questmark.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.questmark.entity.components.PositionComponent;
import com.questmark.entity.components.TextureComponent;
import com.questmark.entity.components.VelocityComponent;

/**
 * A utility class that contains a {@link ComponentMapper} for each Ashley {@link Component}
 * so we can retrieve entity components in O(1) time.
 *
 * @author Ming Li
 */
public final class Mapper {

    public static final ComponentMapper<PositionComponent> POS_MAPPER
            = ComponentMapper.getFor(PositionComponent.class);

    public static final ComponentMapper<VelocityComponent> VEL_MAPPER
            = ComponentMapper.getFor(VelocityComponent.class);

    public static final ComponentMapper<TextureComponent> TEXTURE_MAPPER
            = ComponentMapper.getFor(TextureComponent.class);

}
