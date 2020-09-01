package com.abiyedanagogo.game;

import com.abiyedanagogo.game.screens.PlayScreen;
import com.abiyedanagogo.game.screens.SplashScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NewGame extends Game {

	public static final short GROUND_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short WALL_BIT = 4;
	public static final short GREEN_ORB_BIT = 8;
	public static final short LIFE_BIT = 16;
	public static final short PUNCH_BIT = 32;
	public static final short DESTROYED_BIT = 64;
	public static final short ENEMY_BIT = 128;
	public static final short BOX_BIT = 256;
	public static final short ITEM_BIT = 512;
	public static final short FIREBALL_BIT = 1024;


	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;
	private SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
//		setScreen(new SplashScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}
