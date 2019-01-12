package com.questmark.game

/**
 * Configuration constants for the game such as virtual screen dimensions,
 * version, title, fps, vsync, window properties, etc.
 *
 * @author Ming Li
 */
object Config {

    // virtual dimensions (5:3 aspect ratio)
    const val V_WIDTH = 200
    const val V_HEIGHT = V_WIDTH * 3 / 5
    const val SCALE = 6

    // screen dimensions with scaling
    const val S_WIDTH = V_WIDTH * SCALE
    const val S_HEIGHT = V_HEIGHT * SCALE

    const val NAME = "Questmark"
    const val VERSION = "v0.0.1"
    const val TITLE = "$NAME $VERSION"

    // performance
    const val BACKGROUND_FPS = 10
    const val FOREGROUND_FPS = 60

    const val V_SYNC = false

    const val RESIZABLE = false

}
