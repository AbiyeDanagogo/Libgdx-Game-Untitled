package com.abiyedanagogo.game.sprites;

import com.abiyedanagogo.game.NewGame;
import com.abiyedanagogo.game.screens.PlayScreen;
import com.abiyedanagogo.game.tools.Hud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class Life extends InteractiveTileObject {

    public Life(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(NewGame.LIFE_BIT);
    }

    @Override
    public void interactiveObjectHit(Player player) {
        setCategoryFilter(NewGame.DESTROYED_BIT);
        getCell().setTile(null);
        //Hud.addLife(1);
        //hud.addLife(1);
    }
}
