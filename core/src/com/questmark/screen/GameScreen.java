package com.questmark.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.questmark.entity.ECS;
import com.questmark.entity.Mapper;
import com.questmark.entity.components.PositionComponent;
import com.questmark.entity.entities.Player;
import com.questmark.entity.systems.KeyInputSystem;
import com.questmark.entity.systems.TileMapCollisionSystem;
import com.questmark.game.Questmark;
import com.questmark.input.KeyInput;
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
    private Player player;

    // map
    private TileMapManager tileMapManager;

    public GameScreen(final Questmark game) {
        super(game);

        ecs = new ECS(game.batch, game.res);
        player = ecs.getPlayer();
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
        ecs.getSystem(TileMapCollisionSystem.class).setMapData(tileMapManager.getMapWidth(),
                tileMapManager.getMapHeight(), tileMapManager.getTileSize(), tileMapManager.getCollisionBoxes());
    }

    @Override
    public void update(float dt) {
        updateCamera();
        tileMapManager.update(dt);
    }

    /**
     * Updates the {@link com.badlogic.gdx.graphics.OrthographicCamera} so that it follows the player's
     * current position except when the player moves to the edges of the map.
     */
    private void updateCamera() {
        PositionComponent pc = Mapper.POS_MAPPER.get(player);
        int mw = tileMapManager.getMapWidth();
        int mh = tileMapManager.getMapHeight();
        int ts = tileMapManager.getTileSize();

        if (pc.x <= mw * ts - ((ts / 2 - 1) * ts) && pc.x >= 6 * ts) {
            cam.position.x = pc.x + 8;
        }
        if (pc.y <= mh * ts - ((ts / 4) * ts) && pc.y >= 4 * ts - (ts / 2)) {
            cam.position.y = pc.y + 4;
        }

        cam.update();

        if (pc.x < 6 * ts) cam.position.x = 6 * ts + (ts / 2);
        if (pc.y < 4 * ts - (ts / 2)) cam.position.y = 4 * ts - ((ts / 2 - 1) / 2);
        if (pc.x > mw * ts - 7 * ts) cam.position.x = (mw * ts - 7 * ts) + 8;
        if (pc.y > mh * ts - 4 * ts) cam.position.y = (mh * ts - 4 * ts) + 4;
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
