package com.abiyedanagogo.game.sprites;

import com.abiyedanagogo.game.NewGame;
import com.abiyedanagogo.game.items.ExtraFireBall;
import com.abiyedanagogo.game.screens.PlayScreen;
import com.abiyedanagogo.game.items.ItemDef;
import com.abiyedanagogo.game.items.ExtraLife;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;

public class Box extends InteractiveTileObject {

    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Box(PlayScreen screen, MapObject object) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tileset_newgame");
        fixture.setUserData(this);
        setCategoryFilter(NewGame.BOX_BIT);
    }

    @Override
    public void interactiveObjectHit(Player player) {
        if (getCell().getTile().getId() == BLANK_COIN) {
            System.out.println("Blank Coin");
        }
        else {
            if (object.getProperties().containsKey("life")) {
            screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 30 / NewGame.PPM), ExtraLife.class));
            }
            else if (object.getProperties().containsKey("fireball")) {
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 30 / NewGame.PPM), ExtraFireBall.class));
            }
        }
        getCell().setTile(tileSet.getTile(BLANK_COIN));
    }
}
