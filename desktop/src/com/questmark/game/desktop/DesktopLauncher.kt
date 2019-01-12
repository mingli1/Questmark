package com.questmark.game.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.questmark.game.Config
import com.questmark.game.Questmark

/**
 * Main class for desktop version.
 *
 * @author Ming Li
 */
object DesktopLauncher {

    @JvmStatic
    fun main(args: Array<String>) {
        val config = LwjglApplicationConfiguration()

        config.width = Config.S_WIDTH
        config.height = Config.S_HEIGHT
        config.title = Config.TITLE
        config.resizable = Config.RESIZABLE
        config.vSyncEnabled = Config.V_SYNC
        config.backgroundFPS = Config.BACKGROUND_FPS
        config.foregroundFPS = Config.FOREGROUND_FPS

        LwjglApplication(Questmark(), config)
    }

}
