package com.questmark.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * A manager for loading and rendering tiled maps from a Tiled .tmx format.
 *
 * <p>
 *     A map is split into 3 graphical layers: bottom, middle, and top to add the feeling of
 *     depth to the map for entities. There is also a collision layer that holds a list of
 *     {@link com.badlogic.gdx.maps.MapObject} used for collision detection.
 * </p>
 *
 * <p>
 *     The render order of the {@link TiledMapTileLayer} goes as followed: bottom, middle, top
 *     with the {@link com.questmark.entity.systems.RenderSystem} rendered between middle and top.
 * </p>
 *
 * @author Ming Li
 */
public class TileMapManager implements Disposable {

    // map layer keys
    private static final String BOTTOM_LAYER = "bottom";
    private static final String MIDDLE_LAYER = "middle";
    private static final String TOP_LAYER = "top";
    private static final String COLLISION_LAYER = "collision";

    // file path
    private static final String DIR = "maps/";

    // loading
    private TmxMapLoader mapLoader;

    // data
    private TiledMap tiledMap;

    private TiledMapTileLayer bottom;
    private TiledMapTileLayer middle;
    private TiledMapTileLayer top;
    private TiledMapTileLayer collision;

    // map properties (tile dimensions in pixels, map dimensions in tiles)
    private int tileSize;

    private int mapWidth;
    private int mapHeight;

    // rendering
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera cam;

    private Array<Rectangle> collisionBoxes;

    public TileMapManager(Batch batch, OrthographicCamera cam) {
        this.cam = cam;
        mapLoader = new TmxMapLoader();
        renderer = new OrthogonalTiledMapRenderer(null, 1, batch);
        collisionBoxes = new Array<Rectangle>();
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
        tileSize = tiledMap.getProperties().get("tileSize", Integer.class);
        mapWidth = tiledMap.getProperties().get("mapWidth", Integer.class);
        mapHeight = tiledMap.getProperties().get("mapHeight", Integer.class);

        // map layers
        bottom = (TiledMapTileLayer) tiledMap.getLayers().get(BOTTOM_LAYER);
        middle = (TiledMapTileLayer) tiledMap.getLayers().get(MIDDLE_LAYER);
        top = (TiledMapTileLayer) tiledMap.getLayers().get(TOP_LAYER);
        collision = (TiledMapTileLayer) tiledMap.getLayers().get(COLLISION_LAYER);

        collisionBoxes.clear();
        MapObjects objects = collision.getObjects();
        for (RectangleMapObject rectangeMapObject : objects.getByType(RectangleMapObject.class)) {
            Rectangle collisionBox = rectangeMapObject.getRectangle();
            collisionBoxes.add(collisionBox);
        }

        renderer.setMap(tiledMap);
    }

    public void update(float dt) {
        // update camera
        renderer.setView(cam);

        // allow animated tiles to update
        AnimatedTiledMapTile.updateAnimationBaseTime();
    }

    /** Begin render functions. These must be between SpriteBatch.begin() and SpriteBatch.end() **/

    public void renderBottom() {
        renderer.renderTileLayer(bottom);
    }

    public void renderMiddle() {
        renderer.renderTileLayer(middle);
    }

    public void renderTop() {
        renderer.renderTileLayer(top);
    }

    /** End render functions **/

    @Override
    public void dispose() {
        renderer.dispose();
        tiledMap.dispose();
    }

    /**
     * Returns the list of {@link Rectangle}s associated with the {@link RectangleMapObject}s
     * of this tiled map used for collision detected with an entity's bounding box.
     *
     * @return {@link Array} of {@link Rectangle}
     */
    public Array<Rectangle> getCollisionBoxes() {
        return collisionBoxes;
    }

    /**
     * Returns the size of each tile of this tiled map in pixels.
     *
     * @return the tile size
     */
    public int getTileSize() {
        return tileSize;
    }

    /**
     * Returns the width of this tiled map in number of tiles.
     *
     * @return the map width
     */
    public int getMapWidth() {
        return mapWidth;
    }

    /**
     * Returns the height of this tiled map in number of tiles.
     *
     * @return the map height
     */
    public int getMapHeight() {
        return mapHeight;
    }

}
