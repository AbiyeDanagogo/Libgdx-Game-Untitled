package com.abiyedanagogo.game.screens;

import com.abiyedanagogo.game.NewGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
    private OrthographicCamera gameCam;

    public LevelScreen(final NewGame game) {
        this.game = game;
        gameCam = new OrthographicCamera();
        viewport =  new FitViewport(NewGame.V_WIDTH , NewGame.V_HEIGHT, gameCam);
        gameCam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        stage = new Stage(viewport, game.getBatch());
        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);
        skin.getFont("headingfont").getData().setScale(0.1f);
//        skin.getFont("otherfont").getData().setScale(2f);



        List<String> list = new List<>(skin);
        String[] myArray = {"one", "two", "three but very very very very very very long", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "qwerty", "asdfg", "zxcv", "mnbv", "wertyu", "zxcv", "alp", "mango", "jojo", "killerqueen", "kingcrimson"};
        list.setItems(myArray);


        ScrollPane scrollPane = new ScrollPane(list, skin);


        TextButton buttonPlay = new TextButton("PLAY", skin);
        buttonPlay.pad(15);

        TextButton buttonBack = new TextButton("BACK", skin);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });
        buttonBack.pad(10);

//        Table table = new Table(skin);
//        table.setBounds(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        table.debug();
        Table table = new Table(skin);
//        table.center();
//        table.setPosition(0, 0);
        table.setBounds(0,0,NewGame.V_WIDTH, NewGame.V_HEIGHT);
//        table.setBounds(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        table.setFillParent(true);
        table.debug();

        final float BUTTON_SCALE = 12f;

        //Putting table together
        table.add().width(table.getWidth() / 3f);
        table.add("SELECT LEVEL").width(table.getWidth() / 3f);
        table.add().width(table.getWidth() / 3f).row();
        table.add(scrollPane);
        table.add(buttonPlay).size(buttonPlay.getWidth()/ BUTTON_SCALE, buttonPlay.getHeight()/ BUTTON_SCALE);
        table.add(buttonBack).size(buttonBack.getWidth()/ BUTTON_SCALE, buttonBack.getHeight()/ BUTTON_SCALE).bottom().right();
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
