package com.abiyedanagogo.game.items;

import com.badlogic.gdx.math.Vector2;

/*
 * Created by Abiye Danagogo on 10/07/2020
 * */
public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;
    }
}
