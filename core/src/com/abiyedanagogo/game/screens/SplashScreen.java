package com.abiyedanagogo.game.screens;

import com.abiyedanagogo.game.NewGame;
import com.abiyedanagogo.game.tween.SpriteAccessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

/*
 * Created by Abiye Danagogo on 10/08/2020
 * */
public class SplashScreen implements Screen {
    private NewGame game;
    private Sprite splash;
    private TweenManager tweenManager;

    public SplashScreen(final NewGame game) {
        this.game = game;

        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        Texture splashTexture = new Texture("splash.png");
        splash = new Sprite(splashTexture);
        //splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        splash.setSize(500 , 125);
        splash.setPosition((Gdx.graphics.getWidth() - splash.getWidth()) / 2f, Gdx.graphics.getHeight() / 2f);

        //This lets the splash sprite fade in and fade out after 1 second
        Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.to(splash, SpriteAccessor.ALPHA, 1).target(1).repeatYoyo(1, 1).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                game.setScreen(new MainMenuScreen(game));
            }
        }).start(tweenManager);

        tweenManager.update(Float.MIN_VALUE);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();
        splash.draw(game.getBatch());
        game.getBatch().end();
        tweenManager.update(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();

    }

    @Override
    public void dispose() {
        splash.getTexture().dispose();
    }
}
