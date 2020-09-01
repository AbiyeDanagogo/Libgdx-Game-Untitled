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
import com.badlogic.gdx.utils.viewport.Viewport;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class MainMenuScreen implements Screen {

    private NewGame game;
    private TextureAtlas atlas;
    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton buttonExit, buttonPlay;
    private Label headingLabel;
    private BitmapFont headingFont, otherFont;
    private TweenManager tweenManager;

    private Viewport viewport;

    public MainMenuScreen(final NewGame game) {
        this.game = game;

        viewport =  new FitViewport(NewGame.V_WIDTH , NewGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.getBatch());
        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("ui/button.pack");
        skin = new Skin(atlas);
        table = new Table();
        table.center();
        table.setFillParent(true);
//        table = new Table(skin);
//        table.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        headingFont = new BitmapFont(Gdx.files.internal("gamefont.fnt"));
        otherFont = new BitmapFont();

        headingLabel = new Label("DARYL DANGER", new Label.LabelStyle(headingFont, Color.WHITE));
        headingLabel.setFontScale(0.2f);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("buttonup");
        textButtonStyle.down = skin.getDrawable("buttondown");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = otherFont;

        buttonExit = new TextButton("EXIT", textButtonStyle);
        buttonExit.pad(20);
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        buttonPlay = new TextButton("PLAY", textButtonStyle);
        buttonPlay.pad(20);
        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelScreen(game));
            }
        });



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
        headingFont.dispose();
        otherFont.dispose();
    }

}
