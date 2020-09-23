package com.abiyedanagogo.game.screens;

import com.abiyedanagogo.game.NewGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
    private ScrollPane scrollPane;
    private TextButton play, back;

    public LevelScreen(final NewGame game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);
        skin.getFont("headingfont").getData().setScale(0.2f);
//        skin.getFont("otherfont").getData().setScale(2f);

        table = new Table(skin);
        table.setBounds(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.debug();

        List<String> list = new List<>(skin);
        String[] myArray = {"one", "two", "three but very very very very very very long", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "qwerty", "asdfg", "zxcv", "mnbv", "wertyu", "zxcv", "alp", "mango", "jojo", "killerqueen", "kingcrimson"};
        list.setItems(myArray);



        scrollPane = new ScrollPane(list, skin);


        play = new TextButton("PLAY", skin);
        play.pad(15);

        back = new TextButton("BACK", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        });
        back.pad(10);

        //Putting table together
        table.add().width(table.getWidth() / 3f);
        table.add("SELECT LEVEL").width(table.getWidth() / 3f);
        table.add().width(table.getWidth() / 3f).row();
        table.add(scrollPane).expandY();
        table.add(play).size(play.getWidth()/ 5f, play.getHeight()/ 5f);
        table.add(back).size(back.getWidth()/ 5f, back.getHeight()/ 5f);
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
