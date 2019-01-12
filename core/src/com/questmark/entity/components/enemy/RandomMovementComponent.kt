package com.questmark.entity.components.enemy

import com.badlogic.ashley.core.Component

/**
 * A [Component] of an enemy that represents random movement, meaning the enemy
 * performs a random action of either moving in a random direction or being idle every period of time.
 *
 * @author Ming Li
 */
class RandomMovementComponent(val freq: Float) : Component
