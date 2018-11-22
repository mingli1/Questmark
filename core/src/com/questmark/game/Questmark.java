package com.questmark.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.questmark.util.Resources;

/**
 * The main game class that manages game screens and assets.
 *
 * @author Ming Li
 */
public class Questmark extends Game {

	public Batch batch;
	public Resources res;

	@Override
	public void create() {
		batch = new SpriteBatch();
		res = new Resources();
	}

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		res.dispose();
	}

}
