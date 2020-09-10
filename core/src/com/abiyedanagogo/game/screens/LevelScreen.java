package com.abiyedanagogo.game.screens;

import com.abiyedanagogo.game.NewGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

/*
 * Created by Abiye Danagogo on 25/08/2020
 * */
public class LevelScreen implements Screen {

    private NewGame game;
    private Stage stage;
    private Table table;
    private TextureAtlas atlas;
    private Skin skin;
    private List list;
    private ScrollPane scrollPane;
    private TextButton play, back;

    public LevelScreen(NewGame game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("ui/button.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

        table = new Table(skin);
        table.debug();

        list = new List(skin);
        //list.setItems(new String[] {"one", "two", "three", "and"});
        scrollPane = new ScrollPane(list, skin);

        play = new TextButton("PLAY", skin);
        play.pad(15);

        back = new TextButton("BACK", skin);
        back.pad(10);

        //Putting table together
        table.add("SELECT LEVEL");
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
