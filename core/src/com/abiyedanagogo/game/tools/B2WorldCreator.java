package com.abiyedanagogo.game.tools;

import com.abiyedanagogo.game.NewGame;
import com.abiyedanagogo.game.screens.PlayScreen;
import com.abiyedanagogo.game.sprites.Box;
import com.abiyedanagogo.game.sprites.GreenOrb;
import com.abiyedanagogo.game.sprites.Life;
import com.abiyedanagogo.game.sprites.enemies.Enemy;
import com.abiyedanagogo.game.sprites.enemies.Flyer;
import com.abiyedanagogo.game.sprites.enemies.Monster;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static com.abiyedanagogo.game.NewGame.PPM;

public class B2WorldCreator {

    private Array<Monster> monsters;
    private Array<Flyer> flyers;


    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();

        FixtureDef fdef = new FixtureDef();
        Body body;

        //for ground
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //for walls
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = NewGame.WALL_BIT;
            body.createFixture(fdef);
        }

        //for green orbs
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {

            new GreenOrb(screen, object);
        }

        //for life
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {

            new Life(screen, object);
        }

        //for boxes
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {


            new Box(screen, object);
        }

        //create all monsters
        monsters = new Array<Monster>();
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            monsters.add(new Monster(screen, rect.getX() / PPM, rect.getY() / PPM));
        }

        flyers = new Array<Flyer>();
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            flyers.add(new Flyer(screen, rect.getX() / PPM, rect.getY() / PPM));
        }
    }

    public Array<Monster> getMonsters() {
        return monsters;
    }

    public Array<Enemy> getEnemies() {
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(monsters);
        enemies.addAll(flyers);
        return enemies;
    }
}
