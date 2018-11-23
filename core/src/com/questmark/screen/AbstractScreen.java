package com.questmark.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.questmark.game.Config;
import com.questmark.game.Questmark;

/**
 * A screen template for game states.
 *
 * @author Ming Li
 */
public abstract class AbstractScreen implements Screen {

    // every screen receives a reference to the main game class
    protected final Questmark game;

    // main stage of each screen
    protected Stage stage;
    protected Viewport viewport;
    protected OrthographicCamera cam;

    public AbstractScreen(final Questmark game) {
        this.game = game;

        cam = new OrthographicCamera(Config.V_WIDTH, Config.V_HEIGHT);
        cam.setToOrtho(false);
        viewport = new StretchViewport(Config.V_WIDTH, Config.V_HEIGHT, cam);
        stage = new Stage(viewport, game.batch);
    }

    public abstract void update(float dt);

    @Override
    public void render(float dt) {
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * Returns the stage of this screen.
     *
     * @return
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Returns the universal instance of the game {@link Questmark}.
     *
     * @return
     */
    public Questmark getGame() {
        return game;
    }

}
