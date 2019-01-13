package com.questmark.anim

/**
 * Represents a state, or type, of animation. Regular animations (ie not for the player or entities)
 * have `Normal` state and states specific to entities are denoted accordingly.
 *
 * `strState` is appended to animation sprite sheets names
 * `looping` is whether or not the animation state loops
 *
 * @author Ming Li
 */
enum class AnimationState constructor(private val strState: String, private val looping: Boolean) {

    Normal("", true),

    // entity specific
    Idle("idle", true),
    InCombat("in_combat", true),
    Walking("walking", true),
    
    MeleeAttack("melee_attack", false),
    RangeAttack("range_attack", false);

    override fun toString(): String {
        return strState
    }

    fun isLooping(): Boolean {
        return looping
    }

}