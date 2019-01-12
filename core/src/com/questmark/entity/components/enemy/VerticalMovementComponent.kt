package com.questmark.entity.components.enemy

import com.badlogic.ashley.core.Component

/**
 * Represents an enemy that moves in a constant vertical motion, changing directions
 * after a certain time period.
 *
 * @author Ming Li
 */
class VerticalMovementComponent(val dist: Float) : Component
