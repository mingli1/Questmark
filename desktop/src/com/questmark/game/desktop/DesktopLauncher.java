package com.questmark.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.questmark.game.Config;
import com.questmark.game.Questmark;

/**
 * Main class for desktop version.
 *
 * @author Ming Li
 */
public class DesktopLauncher {

	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = Config.S_WIDTH;
		config.height = Config.S_HEIGHT;
		config.title = Config.TITLE;
		config.resizable = Config.RESIZABLE;
		config.vSyncEnabled = Config.VSYNC;
		config.backgroundFPS = Config.BACKGROUND_FPS;
		config.foregroundFPS = Config.FOREGROUND_FPS;

		new LwjglApplication(new Questmark(), config);
	}

}
