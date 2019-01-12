package com.questmark.util

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.JsonReader

import java.util.HashMap

/**
 * Main resource loading and storage class. Uses an AssetManager to manage textures, sounds,
 * musics, etc. Contains convenience methods to load and get resources from the asset manager.
 *
 * @author Ming Li
 */
class Resources : Disposable {

    private val manager: AssetManager = AssetManager()
    private val jsonReader: JsonReader = JsonReader()

    // all graphical assets lie in one atlas
    private val atlas: TextureAtlas

    // variety of texture representations
    private val single: Map<String, TextureRegion>
    private val multiple: Map<String, Array<TextureRegion>>
    private val sheet: Map<String, Array<Array<TextureRegion>>>

    // main font of the game
    val font: BitmapFont

    init {
        single = HashMap()
        multiple = HashMap()
        sheet = HashMap()

        // load assets here and store in maps
        manager.load("textures/textures.atlas", TextureAtlas::class.java)

        manager.finishLoading()

        font = BitmapFont()
        atlas = manager.get("textures/textures.atlas", TextureAtlas::class.java)

        // mapping textures
        single["player"] = atlas.findRegion("test_player")
        single["enemy"] = atlas.findRegion("test_enemy")
        single["colonel"] = atlas.findRegion("colonel")
        single["luffy"] = atlas.findRegion("luffy")
        single["cat"] = atlas.findRegion("cat")
        single["dog"] = atlas.findRegion("dog")
        single["nil"] = atlas.findRegion("nil")
    }

    /**
     * Returns the [TextureRegion] that is a single texture associated with a given key.
     *
     * @param key the name of the asset
     * @return
     */
    fun getSingleTexture(key: String): TextureRegion? {
        return single[key]
    }

    /**
     * Returns a 1d array of [TextureRegion] associated with a given key.
     *
     * @param key the name of the asset
     * @return
     */
    fun getMultipleTextures(key: String): Array<TextureRegion>? {
        return multiple[key]
    }

    /**
     * Returns a 2d array of [TextureRegion] associated with a given key.
     *
     * @param key the name of the asset
     * @return
     */
    fun getTextureSheet(key: String): Array<Array<TextureRegion>>? {
        return sheet[key]
    }

    override fun dispose() {
        manager.dispose()
        atlas.dispose()
        font.dispose()
    }

}
