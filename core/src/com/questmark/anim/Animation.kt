package com.questmark.anim

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import java.lang.IllegalArgumentException

/**
 * Represents an animation manager that handles [AnimationState]
 * All animations are created with LibGDX's [Animation<TextureRegion>]
 *
 * @author Ming Li
 */
class Animation {

    var stateTime: Float = 0f
        private set

    lateinit var currKeyFrame: TextureRegion private set
    private var currAnimState: AnimationState = AnimationState.Normal

    private var anims: MutableMap<AnimationState, Animation<TextureRegion>> = HashMap()

    /**
     * Creates an animation with only one keyframe (ie no animation)
     *
     * @param frame the [TextureRegion] keyframe
     */
    constructor(frame: TextureRegion) {
        anims[currAnimState] = Animation(0f, frame)
    }

    /**
     * Creates a [AnimationState] animation given an array of [TextureRegion] and delay
     * Animation state can also be specified but `Normal` is the default
     * Note: the array is LibGDX's [Array]
     *
     * @param frames the keyframes
     * @param delay the delay
     */
    constructor(frames: Array<TextureRegion>, delay: Float, state: AnimationState = AnimationState.Normal) {
        anims[state] = Animation(delay, frames)
        this.currAnimState = state
    }

    /**
     * Creates an animation of an indexed 2d array of [TextureRegion] from a spritesheet.
     * Index represents the row and numFrames is how many frames are in the animation.
     * Animation state can also be specified but `Normal` is the default
     *
     * @param sheet 2d Kotlin array spritesheet
     * @param numFrames the num of frames in the anim
     * @param index the row index
     * @param delay the delay
     */
    constructor(sheet: kotlin.Array<kotlin.Array<TextureRegion>>,
                numFrames: Int, index: Int, delay: Float, state: AnimationState = AnimationState.Normal) {
        val frames = arrayOfNulls<TextureRegion>(numFrames)
        for (i in 0 until numFrames) {
            frames[i] = sheet[index][i]
        }
        anims[state] = Animation(delay, Array<TextureRegion>(frames))
        this.currAnimState = state
    }

    /**
     * Creates an animation with multiple animation states
     *
     * @param mapping a map of animation state to a pair of frames and delays
     * @param state the initial animation state
     */
    constructor(mapping: Map<AnimationState, Pair<kotlin.Array<TextureRegion>, Float>>, state: AnimationState) {
        for ((state, pair) in mapping) {
            anims[state] = Animation(pair.second, Array<TextureRegion>(pair.first))
        }
        this.currAnimState = state
    }

    fun update(dt: Float) {
        stateTime += dt
        if (anims[currAnimState] != null) {
            currKeyFrame = anims[currAnimState]!!.getKeyFrame(stateTime, currAnimState.isLooping())
        }
    }

    /**
     * Sets the current animation state to a given state.
     *
     * @param state the given state
     * @throws IllegalArgumentException if no animations exist for given state
     */
    @Throws(IllegalArgumentException::class)
    fun setState(state: AnimationState) {
        stateTime = 0f
        if (anims[state] == null) throw IllegalArgumentException("No animations for given animation state.")
        this.currAnimState = state
    }

    /**
     * Returns whether or not the animation is finished
     */
    fun isFinished(): Boolean {
        return anims[currAnimState]!!.isAnimationFinished(stateTime)
    }

}