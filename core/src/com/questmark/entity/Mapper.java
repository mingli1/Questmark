package com.questmark.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.questmark.entity.components.*;
import com.questmark.entity.components.enemy.*;

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

    public static final ComponentMapper<DimensionComponent> SIZE_MAPPER
            = ComponentMapper.getFor(DimensionComponent.class);

    // enemy specific components

    public static final ComponentMapper<AggressionComponent> AGGRESSION_MAPPER
            = ComponentMapper.getFor(AggressionComponent.class);

    public static final ComponentMapper<SourcePositionComponent> SOURCE_POS_MAPPER
            = ComponentMapper.getFor(SourcePositionComponent.class);

    public static final ComponentMapper<CircularMovementComponent> CIR_MOVE_MAPPER
            = ComponentMapper.getFor(CircularMovementComponent.class);

    public static final ComponentMapper<HorizontalMovementComponent> HOR_MOVE_MAPPER
            = ComponentMapper.getFor(HorizontalMovementComponent.class);

    public static final ComponentMapper<VerticalMovementComponent> VER_MOVE_MAPPER
            = ComponentMapper.getFor(VerticalMovementComponent.class);

    public static final ComponentMapper<RandomMovementComponent> RAND_MOVE_MAPPER
            = ComponentMapper.getFor(RandomMovementComponent.class);

}
