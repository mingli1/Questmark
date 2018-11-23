package com.questmark.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.questmark.entity.ECS;
import com.questmark.game.Config;
import com.questmark.game.Questmark;
import com.questmark.map.TileMapManager;

/**
 * Represents the screen where all game play and map events are handled.
 *
 * @author Ming Li
 */
public class GameScreen extends AbstractScreen {

    // entity
    private ECS ecs;

    // camera that follows the player and is the main game camera
    private OrthographicCamera cam;

    // map
    private TileMapManager tileMapManager;

    public GameScreen(final Questmark game) {
        super(game);

        cam = new OrthographicCamera(Config.V_WIDTH, Config.V_HEIGHT);
        cam.setToOrtho(false);

        ecs = new ECS(game.batch, game.res);
        tileMapManager = new TileMapManager(game.batch, cam);
    }

    @Override
    public void show() {
        // load map on show
        tileMapManager.load("test");
    }

    @Override
    public void update(float dt) {
        cam.update();
        tileMapManager.update(dt);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();

        tileMapManager.renderBottom();
        tileMapManager.renderMiddle();
        ecs.update(dt);
        tileMapManager.renderTop();

        game.batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        ecs.dispose();
        tileMapManager.dispose();
    }

}
