package com.abiyedanagogo.game.screens;

import com.abiyedanagogo.game.NewGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/*
 * Created by Abiye Danagogo on 25/08/2020
 * */
public class LevelScreen implements Screen {

    private NewGame game;
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Viewport viewport;

    public LevelScreen(final NewGame game) {
        this.game = game;
        viewport =  new FitViewport(NewGame.V_WIDTH , NewGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.getBatch());
        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);
        skin.getFont("headingfont").getData().setScale(0.15f);
        skin.getFont("otherfont").getData().setScale(0.08f);
        skin.getFont("systemfont").getData().setScale(0.8f);

        //The list of items in the scroll pane is declared and initialised.
        List<String> list = new List<>(skin);
        list.setItems("first", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "qwerty", "asdfg", "mnbv", "wertyu", "zxcv", "alp", "mango", "jojo", "killerqueen", "kingcrimson");

        //The scroll pane is declared and initialised.
        ScrollPane scrollPane = new ScrollPane(list, skin);

        //The play button is declared and initialised.
        TextButton buttonPlay = new TextButton("PLAY", skin);
        buttonPlay.pad(15);

        //The back button is declared and initialised.
        TextButton buttonBack = new TextButton("BACK", skin);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });
        buttonBack.pad(15);

        //The table that will hold the labels, buttons and scroll pane is declared and initialised
        Table table = new Table(skin);
        table.setBounds(0,0, NewGame.V_WIDTH, NewGame.V_HEIGHT);
        table.setFillParent(true);
        table.debug();

        //This constant is used for scaling the size of the buttons.
        final float BUTTON_SCALE = 8f;

        //The heading with the title SELECT LEVEL is put first into the table.
        table.add("SELECT LEVEL").colspan(3);
        //A new row is created
        table.row();
        //The remaining items are added into the table.
        table.add(scrollPane).expandY().uniformX().left().top();
        table.add(buttonPlay).size(buttonPlay.getWidth()/ BUTTON_SCALE, buttonPlay.getHeight()/ BUTTON_SCALE).uniformX();
        table.add(buttonBack).size(buttonBack.getWidth()/ BUTTON_SCALE, buttonBack.getHeight()/ BUTTON_SCALE).bottom().right().uniformX();

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        atlas.dispose();
        skin.dispose();
    }
}
