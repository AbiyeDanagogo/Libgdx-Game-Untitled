package com.abiyedanagogo.game.screens;

import com.abiyedanagogo.game.items.ExtraFireBall;
import com.abiyedanagogo.game.sprites.enemies.Enemy;
import com.abiyedanagogo.game.tools.Controller;
import com.abiyedanagogo.game.NewGame;
import com.abiyedanagogo.game.sprites.Player;
import com.abiyedanagogo.game.tools.B2WorldCreator;
import com.abiyedanagogo.game.tools.Hud;
import com.abiyedanagogo.game.tools.WorldContactListener;
import com.abiyedanagogo.game.items.Item;
import com.abiyedanagogo.game.items.ItemDef;
import com.abiyedanagogo.game.items.ExtraLife;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.concurrent.LinkedBlockingQueue;

import static com.abiyedanagogo.game.NewGame.PPM;
import static com.abiyedanagogo.game.NewGame.V_HEIGHT;
import static com.abiyedanagogo.game.NewGame.V_WIDTH;

/*
* Created by Abiye Danagogo on 29/06/2020
* */
public class PlayScreen implements Screen {
    //Reference to our Game, used to set Screens
    private NewGame game;

    //Camera and viewport
    private OrthographicCamera gameCam;
    private Viewport gamePort;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    private WorldContactListener listener;

    private Player player;
    private Controller controller;
    private Hud hud;

    //Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;


    public PlayScreen(NewGame game) {
        this.game = game;
        //create camera used to follow the player through the world
        gameCam = new OrthographicCamera();
        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(V_WIDTH / PPM, V_HEIGHT / PPM, gameCam);
        //gamePort = new FitViewport(4000 / PPM, 1000 / PPM, gameCam);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, -10), true);
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();
        //b2dr.setDrawBodies(false);

        //The controller displays an onscreen controller on screen that is used to control the player if played on a mobile device
        controller = new Controller(game.getBatch());
        //create our game HUD for scores/timers/level info
        hud = new Hud(game.getBatch());

        //Load our map and setup our map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("newgametile1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/ PPM);

        //initially set our gamcam to be centered correctly at the start of of map
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //This creates most of the sprites and objects into our game world
        creator = new B2WorldCreator(this);
        //create our player into the game world
        player = new Player(this);

        listener = new WorldContactListener();
        world.setContactListener(listener);

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
    }

    public void spawnItem(ItemDef idef) {
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems() {
        if (!itemsToSpawn.isEmpty()) {
            ItemDef idef = itemsToSpawn.poll();
            if (idef.type == ExtraLife.class) {
                items.add(new ExtraLife(this, idef.position.x, idef.position.y));
            }
            else if (idef.type == ExtraFireBall.class) {
                items.add(new ExtraFireBall(this, idef.position.x, idef.position.y));
            }
        }
    }


    @Override
    public void show() {

    }

    /*
    * This method is used to perform actions when an input is received.
    * */
    public void  handleInput() {
        if (controller.isRightPressed()) {
            if (!player.isPlayerHit())
                player.getB2body().setLinearVelocity(new Vector2(3, player.getB2body().getLinearVelocity().y));
        }
        else if (controller.isLeftPressed()) {
            if (!player.isPlayerHit())
                player.getB2body().setLinearVelocity(new Vector2(-3, player.getB2body().getLinearVelocity().y));
        }
        else
            player.getB2body().setLinearVelocity(new Vector2(0, player.getB2body().getLinearVelocity().y));
        if (controller.isUpPressed() && player.getB2body().getLinearVelocity().y == 0 && !controller.isInAir()){
            player.getB2body().setLinearVelocity(new Vector2(0, 5));
            controller.setInAir(true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.setPlayerPunching(true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            player.fire();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            System.out.println("Abiye is the developer");
        }

    }

    public void update(float dt) {

        handleInput();
        handleSpawningItems();

        player.update(dt);

        for (Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);

            if (enemy.getB2body() != null) {
                if (Math.abs(enemy.getB2body().getPosition().x - player.getB2body().getPosition().x) < (250 / PPM))
                    enemy.getB2body().setActive(true);
                if (Math.abs(enemy.getB2body().getPosition().x - player.getB2body().getPosition().x) < (150 / PPM))
                    enemy.setDetected(true);
                else if (Math.abs(enemy.getB2body().getPosition().x - player.getB2body().getPosition().x) > (150 / PPM))
                    enemy.setDetected(false);
            }
            /*if (enemy == creator.getMonsters().get(0)) {
                //System.out.println(enemy.b2body.getLinearVelocity().x+"   "+ ((Monster)enemy).walkingRight+"  "+((Monster)enemy).isFlipX());
                System.out.println(player.getX()+" "+ player.b2body.getPosition().x);
            }*/
        }


        for (Item item : items)
            item.update(dt);

        world.step(1/60f, 6, 2);
        //gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        gameCam.position.y = gamePort.getWorldHeight() / 2 ;
        gameCam.position.x = player.getB2body().getPosition().x;
        //gameCam.position.x = player.b2body.getPosition().x - 140 / PPM;
        gameCam.update();
        renderer.setView(gameCam);


    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        b2dr.render(world, gameCam.combined);

        game.getBatch().setProjectionMatrix(gameCam.combined);
        game.getBatch().begin();

        player.draw(game.getBatch());

        for (Enemy enemy : creator.getEnemies())
            enemy.draw(game.getBatch());

        for (Item item : items)
            item.draw(game.getBatch());


        game.getBatch().end();



        game.getBatch().setProjectionMatrix(controller.getStage().getCamera().combined);
        //controller.draw();

        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        controller.resize(width, height);
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

    /*
    * Disposes all opened resources
    * */
    @Override
    public void dispose() {
        //game.batch.dispose();
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        controller.dispose();
        hud.dispose();
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public Hud getHud() {
        return hud;
    }

    public Player getPlayer() {
        return player;
    }


}
