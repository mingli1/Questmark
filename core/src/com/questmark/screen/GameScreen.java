package com.questmark.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.questmark.entity.ECS;
import com.questmark.entity.systems.KeyInputSystem;
import com.questmark.game.Questmark;
import com.questmark.input.KeyInput;
import com.questmark.input.KeyInputHandler;
import com.questmark.map.TileMapManager;

/**
 * Represents the screen where all game play and map events are handled.
 *
 * @author Ming Li
 */
public class GameScreen extends AbstractScreen {

    // input
    private KeyInput input;

    // entity
    private ECS ecs;

    // map
    private TileMapManager tileMapManager;

    public GameScreen(final Questmark game) {
        super(game);

        ecs = new ECS(game.batch, game.res);
        tileMapManager = new TileMapManager(game.batch, cam);

        // player input system
        KeyInputSystem inputSystem = new KeyInputSystem();
        input = new KeyInput(inputSystem);
        ecs.addSystem(inputSystem);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(input);
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
        update(dt);

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
