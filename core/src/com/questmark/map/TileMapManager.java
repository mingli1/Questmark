package com.questmark.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * A manager for loading and rendering tiled maps from a Tiled .tmx format.
 *
 * @author Ming Li
 */
public class TileMapManager implements Disposable {

    // file path
    private static final String DIR = "maps/";

    // loading
    private TmxMapLoader mapLoader;

    // data
    private TiledMap tiledMap;
    private Array<TiledMapTileLayer> mapLayers;

    // map properties
    private int tileWidth;
    private int tileHeight;

    private int mapWidth;
    private int mapHeight;

    // rendering
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera cam;

    public TileMapManager(Batch batch, OrthographicCamera cam) {
        this.cam = cam;
        mapLoader = new TmxMapLoader();
        renderer = new OrthogonalTiledMapRenderer(null, 1, batch);
    }

    /**
     * The tile map manager will be given a map name, which is the name of the tiled map
     * (not the file path). The filepath will be in some format "maps/[mapName].tmx"
     *
     * @param mapName name of the map
     */
    public void load(String mapName) {
        tiledMap = mapLoader.load(DIR + mapName + ".tmx");

        // retrieve data from set properties in the map files
        tileWidth = tiledMap.getProperties().get("tileWidth", Integer.class);
        tileHeight = tiledMap.getProperties().get("tileHeight", Integer.class);
        mapWidth = tiledMap.getProperties().get("mapWidth", Integer.class);
        mapHeight = tiledMap.getProperties().get("mapHeight", Integer.class);

        mapLayers = new Array<TiledMapTileLayer>();
        for (MapLayer layer : tiledMap.getLayers()) {
            mapLayers.add((TiledMapTileLayer) layer);
        }

        renderer.setMap(tiledMap);
    }

    public void update(float dt) {
        // update camera
        renderer.setView(cam);
    }

    /**
     * Note: this must be between SpriteBatch.begin() and end
     */
    public void render(float dt) {
        // allow animated tiles to update
        AnimatedTiledMapTile.updateAnimationBaseTime();

        // render tile layers
        for (TiledMapTileLayer layer : mapLayers) {
            if (layer != null) {
                renderer.renderTileLayer(layer);
            }
        }
    }

    @Override
    public void dispose() {
        renderer.dispose();
        tiledMap.dispose();
    }

}
