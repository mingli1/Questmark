package com.questmark.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The main game class that manages game screens and assets.
 *
 * @author Ming Li
 */
public class Questmark extends Game {

	public Batch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();
	}

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
	}

}
