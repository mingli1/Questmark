package com.questmark.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Main resource loading and storage class. Uses an AssetManager to manage textures, sounds,
 * musics, etc. Contains convenience methods to load and get resources from the asset manager.
 *
 * @author Ming Li
 */
public class Resources implements Disposable {

    private AssetManager manager;
    private JsonReader jsonReader;

    // all graphical assets lie in one atlas
    private TextureAtlas atlas;

    // variety of texture representations
    private Map<String, TextureRegion> single;
    private Map<String, TextureRegion[]> multiple;
    private Map<String, TextureRegion[][]> sheet;

    // main font of the game
    private BitmapFont font;

    public Resources() {
        manager = new AssetManager();
        jsonReader = new JsonReader();

        single = new HashMap<String, TextureRegion>();
        multiple = new HashMap<String, TextureRegion[]>();
        sheet = new HashMap<String, TextureRegion[][]>();

        // load assets here and store in maps
        manager.load("textures/textures.atlas", TextureAtlas.class);

        manager.finishLoading();

        font = new BitmapFont();
        atlas = manager.get("textures/textures.atlas", TextureAtlas.class);

        // mapping textures
        single.put("player", atlas.findRegion("test_player"));
    }

    /**
     * Returns the {@link TextureRegion} that is a single texture associated with a given key.
     *
     * @param key the name of the asset
     * @return
     */
    public TextureRegion getSingleTexture(String key) {
        return single.get(key);
    }

    /**
     * Returns a 1d array of {@link TextureRegion} associated with a given key.
     *
     * @param key the name of the asset
     * @return
     */
    public TextureRegion[] getMultipleTextures(String key) {
        return multiple.get(key);
    }

    /**
     * Returns a 2d array of {@link TextureRegion} associated with a given key.
     *
     * @param key the name of the asset
     * @return
     */
    public TextureRegion[][] getTextureSheet(String key) {
        return sheet.get(key);
    }

    /**
     * Returns the main font of the game as a {@link BitmapFont}.
     *
     * @return
     */
    public BitmapFont getFont() {
        return font;
    }

    @Override
    public void dispose() {
        manager.dispose();
        atlas.dispose();
        font.dispose();
    }

}
