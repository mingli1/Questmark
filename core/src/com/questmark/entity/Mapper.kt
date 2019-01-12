package com.questmark.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.questmark.entity.components.*
import com.questmark.entity.components.enemy.*

/**
 * A utility class that contains a [ComponentMapper] for each Ashley [Component]
 * so we can retrieve entity components in O(1) time.
 *
 * @author Ming Li
 */
object Mapper {

    val POS_MAPPER: ComponentMapper<PositionComponent>? = ComponentMapper.getFor(PositionComponent::class.java)

    val PREV_POS_MAPPER: ComponentMapper<PreviousPositionComponent>? = ComponentMapper.getFor(PreviousPositionComponent::class.java)

    val VEL_MAPPER: ComponentMapper<VelocityComponent>? = ComponentMapper.getFor(VelocityComponent::class.java)

    val TEXTURE_MAPPER: ComponentMapper<TextureComponent>? = ComponentMapper.getFor(TextureComponent::class.java)

    val BOUNDING_BOX_MAPPER: ComponentMapper<BoundingBoxComponent>? = ComponentMapper.getFor(BoundingBoxComponent::class.java)

    val SPEED_MAPPER: ComponentMapper<SpeedComponent>? = ComponentMapper.getFor(SpeedComponent::class.java)

    val SIZE_MAPPER: ComponentMapper<DimensionComponent>? = ComponentMapper.getFor(DimensionComponent::class.java)

    // enemy specific components

    val AGGRESSION_MAPPER: ComponentMapper<AggressionComponent>? = ComponentMapper.getFor(AggressionComponent::class.java)

    val SOURCE_POS_MAPPER: ComponentMapper<SourcePositionComponent>? = ComponentMapper.getFor(SourcePositionComponent::class.java)

    val CIR_MOVE_MAPPER: ComponentMapper<CircularMovementComponent>? = ComponentMapper.getFor(CircularMovementComponent::class.java)

    val HOR_MOVE_MAPPER: ComponentMapper<HorizontalMovementComponent>? = ComponentMapper.getFor(HorizontalMovementComponent::class.java)

    val VER_MOVE_MAPPER: ComponentMapper<VerticalMovementComponent>? = ComponentMapper.getFor(VerticalMovementComponent::class.java)

    val RAND_MOVE_MAPPER: ComponentMapper<RandomMovementComponent>?= ComponentMapper.getFor(RandomMovementComponent::class.java)

}
