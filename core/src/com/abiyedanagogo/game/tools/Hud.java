package com.abiyedanagogo.game.tools;

import com.abiyedanagogo.game.NewGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/*
 * Created by Abiye Danagogo on 02/07/2020
 * */
public class Hud implements Disposable {
    private Stage stage;
    private Viewport viewport;

    private TextureAtlas atlas;
    private Skin skin;

    private Integer lifeCount;
    private Integer scoreCount;

    private Label scoreLabel;
    private Label scoreCountLabel;
    private Label lifeCountLabel;
    private Label pauseLabel;
    private Label fireballCountLabel;

    public Hud(SpriteBatch sb) {

        atlas = new TextureAtlas("ui/atlas.pack");
        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"),atlas);
        skin.getFont("headingfont").getData().setScale(0.3f);
        skin.getFont("otherfont").getData().setScale(0.15f);

        lifeCount = 4;
        scoreCount = 0;

        viewport = new FitViewport(NewGame.V_WIDTH, NewGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        scoreLabel = new Label("SCORE", skin);
        scoreCountLabel = new Label(String.valueOf(scoreCount), skin, "other");

        lifeCountLabel = new Label("" + lifeCount, skin, "other");
        lifeCountLabel.setAlignment(Align.bottom);

        fireballCountLabel = new Label(String.valueOf(lifeCount), skin, "other");
        fireballCountLabel.setAlignment(Align.bottom);

        pauseLabel = new Label("||", skin, "other");

        Image heartIcon = new Image(new Texture("hearticon.png"));
        heartIcon.setSize(44.1f, 40);
        heartIcon.setScale(0.6f);

        Stack lifeStack = new Stack();
        lifeStack.add(heartIcon);
        lifeStack.add(lifeCountLabel);

        Image fireballIcon = new Image(new Texture("fireballicon.png"));
        fireballIcon.setSize(42, 42);
        fireballIcon.setScale(0.6f);

        Stack fireballStack = new Stack();
        fireballStack.add(fireballIcon);
        fireballStack.add(fireballCountLabel);

        table.add(lifeStack).size(heartIcon.getWidth(), heartIcon.getHeight()).expandX().padTop(10);
        table.add(fireballStack).size(fireballIcon.getWidth(), fireballIcon.getHeight()).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(5);
        table.add().expandX().size(fireballIcon.getWidth(), fireballIcon.getHeight()).expandX().padTop(10);
        table.add(pauseLabel).size(heartIcon.getWidth(), heartIcon.getHeight()).expandX().padTop(10);
        table.row();
        table.add().expandX();
        table.add().expandX();
        table.add(scoreCountLabel).expandX();
        table.add().expandX();
        table.add().expandX();

        stage.addActor(table);
    }

    //This method adds the parameter "value" to the scoreCount whenever it is called
    public void addScore(int value) {
        scoreCount += value;
        scoreCountLabel.setText(String.valueOf(scoreCount));
    }
    //This method adds the parameter value to the lifeCount whenever it is called
    public void addLife(int value) {
        lifeCount += value;
        lifeCountLabel.setText(String.valueOf(lifeCount));
    }

    //This sets fireballCountLabel to whatever the string value of parameter "i" is assigned to.
    public void fireballCounter(int i) {
        fireballCountLabel.setText(String.valueOf(i));
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose() {
        stage.dispose();
        atlas.dispose();
        skin.dispose();
    }
}
