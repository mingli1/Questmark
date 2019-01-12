package com.questmark.entity.components.enemy

import com.badlogic.ashley.core.Component

/**
 * Represents an enemy that moves in a constant horizontal motion, changing directions
 * after a certain time period.
 *
 * @author Ming Li
 */
class HorizontalMovementComponent(val dist: Float) : Component
