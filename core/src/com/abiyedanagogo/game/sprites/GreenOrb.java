package com.abiyedanagogo.game.sprites;

import com.abiyedanagogo.game.NewGame;
import com.abiyedanagogo.game.screens.PlayScreen;
import com.abiyedanagogo.game.tools.Hud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.abiyedanagogo.game.NewGame.PPM;

public class GreenOrb extends InteractiveTileObject {
    public GreenOrb(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(NewGame.GREEN_ORB_BIT);
    }

    @Override
    public void interactiveObjectHit(Player player) {
        setCategoryFilter(NewGame.DESTROYED_BIT);
        getCell().setTile(null);
        //Hud.addScore(200);
        hud.addScore(200);
    }
}
