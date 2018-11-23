package com.questmark.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.questmark.entity.components.*;

/**
 * A utility class that contains a {@link ComponentMapper} for each Ashley {@link Component}
 * so we can retrieve entity components in O(1) time.
 *
 * @author Ming Li
 */
public final class Mapper {

    public static final ComponentMapper<PositionComponent> POS_MAPPER
            = ComponentMapper.getFor(PositionComponent.class);

    public static final ComponentMapper<PreviousPositionComponent> PREV_POS_MAPPER
            = ComponentMapper.getFor(PreviousPositionComponent.class);

    public static final ComponentMapper<VelocityComponent> VEL_MAPPER
            = ComponentMapper.getFor(VelocityComponent.class);

    public static final ComponentMapper<TextureComponent> TEXTURE_MAPPER
            = ComponentMapper.getFor(TextureComponent.class);

    public static final ComponentMapper<BoundingBoxComponent> BOUNDING_BOX_MAPPER
            = ComponentMapper.getFor(BoundingBoxComponent.class);

    public static final ComponentMapper<SpeedComponent> SPEED_MAPPER
            = ComponentMapper.getFor(SpeedComponent.class);

}
