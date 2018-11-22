package com.questmark.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.questmark.entity.ECS;
import com.questmark.util.Resources;

/**
 * The main game class that manages game screens and assets.
 *
 * @author Ming Li
 */
public class Questmark extends Game {

	public Batch batch;
	public Resources res;
	public ECS ecs;

	@Override
	public void create() {
		batch = new SpriteBatch();
		res = new Resources();
		ecs = new ECS(batch, res);
	}

	@Override
	public void render() {
		super.render();
		ecs.update(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		res.dispose();
		ecs.dispose();
	}

}
