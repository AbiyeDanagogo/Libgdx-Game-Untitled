package com.abiyedanagogo.game.screens;

import com.abiyedanagogo.game.tween.ActorAccessor;
import com.abiyedanagogo.game.NewGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

/*
 * Created by Abiye Danagogo on 12/08/2020
 * */
public class MainMenuScreen implements Screen {

    private NewGame game;
    private TextureAtlas atlas;
    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton buttonExit, buttonPlay;
    private Label headingLabel;
    private TweenManager tweenManager;

    private Viewport viewport;

    public MainMenuScreen(final NewGame game) {
        this.game = game;
        viewport =  new FitViewport(NewGame.V_WIDTH , NewGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.getBatch());
        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("ui/button.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"),atlas);

        //The heading
        headingLabel = new Label("DARYL DANGER", skin);
        headingLabel.setFontScale(0.2f);

        //The Play button
        buttonPlay = new TextButton("PLAY", skin);
        buttonPlay.pad(20);
        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelScreen(game));
            }
        });

        //The exit button
        buttonExit = new TextButton("EXIT", skin);
        buttonExit.pad(20);
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //The table is initialised
        table = new Table();
        table.center();
        table.setFillParent(true);

        //The properties i.e the heading, play button and exit button are placed unto the table
        table.add(headingLabel);
        table.row();
        table.add(buttonPlay).size(648/12f, 213/12f);
        table.row();
        table.add(buttonExit).size(648/12f, 213/12f);
//        table.debug();
        stage.addActor(table);

        // TweenManager for animations
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());

        //Heading color animation
        Timeline.createSequence().beginSequence()
                .push(Tween.to(headingLabel, ActorAccessor.RGB, 0.5f).target(0,0,1))
                .push(Tween.to(headingLabel, ActorAccessor.RGB, 0.5f).target(0,1,0))
                .push(Tween.to(headingLabel, ActorAccessor.RGB, 0.5f).target(1,0,0))
                .push(Tween.to(headingLabel, ActorAccessor.RGB, 0.5f).target(1,1,0))
                .push(Tween.to(headingLabel, ActorAccessor.RGB, 0.5f).target(0,1,1))
                .push(Tween.to(headingLabel, ActorAccessor.RGB, 0.5f).target(1,0,1))
                .push(Tween.to(headingLabel, ActorAccessor.RGB, 0.5f).target(1,1,1))
                .end().repeat(Tween.INFINITY, 0).start(tweenManager);

        //Heading and buttons fade in
        Timeline.createSequence().beginSequence()
                .push(Tween.set(buttonPlay, ActorAccessor.ALPHA).target(0))
                .push(Tween.set(buttonExit, ActorAccessor.ALPHA).target(0))
                .push(Tween.from(headingLabel, ActorAccessor.ALPHA, 0.5f).target(0))  //Add new animation to timeline
                .push(Tween.to(buttonPlay, ActorAccessor.ALPHA, 0.5f).target(1))
                .push(Tween.to(buttonExit, ActorAccessor.ALPHA, 0.5f).target(1))
                .end().start(tweenManager);

        //table fade in
        Tween.from(table, ActorAccessor.ALPHA, 0.5f).target(0).start(tweenManager);
        Tween.from(table, ActorAccessor.Y, 0.5f).target(Gdx.graphics.getHeight() / 8f).start(tweenManager);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        atlas.dispose();
        skin.dispose();
    }

}
