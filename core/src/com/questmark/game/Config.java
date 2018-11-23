package com.questmark.game;

/**
 * Configuration constants for the game such as virtual screen dimensions,
 * version, title, fps, vsync, window properties, etc.
 *
 * @author Ming Li
 */
public final class Config {

    // virtual dimensions (5:3 aspect ratio)
    public static final int V_WIDTH = 200;
    public static final int V_HEIGHT = V_WIDTH * 3 / 5;
    public static final int SCALE = 6;

    // screen dimensions with scaling
    public static final int S_WIDTH = V_WIDTH * SCALE;
    public static final int S_HEIGHT = V_HEIGHT * SCALE;

    public static final String NAME = "Questmark";
    public static final String VERSION = "v0.0.1";
    public static final String TITLE = NAME + " " + VERSION;

    // performance
    public static final int BACKGROUND_FPS = 10;
    public static final int FOREGROUND_FPS = 60;

    public static final boolean VSYNC = false;

    public static final boolean RESIZABLE = false;

}
