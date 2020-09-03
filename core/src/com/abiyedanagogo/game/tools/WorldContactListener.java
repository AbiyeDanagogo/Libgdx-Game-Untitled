package com.abiyedanagogo.game.tools;

import com.abiyedanagogo.game.NewGame;
import com.abiyedanagogo.game.items.Item;
import com.abiyedanagogo.game.sprites.enemies.Enemy;
import com.abiyedanagogo.game.sprites.InteractiveTileObject;
import com.abiyedanagogo.game.sprites.Player;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/*
 * Created by Abiye Danagogo on 04/07/2020
 * */
public class WorldContactListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case NewGame.PLAYER_BIT | NewGame.BOX_BIT:
            case NewGame.PLAYER_BIT | NewGame.LIFE_BIT:
            case NewGame.PLAYER_BIT | NewGame.GREEN_ORB_BIT:
                if (fixA.getFilterData().categoryBits == NewGame.PLAYER_BIT) {
                    ((InteractiveTileObject) fixB.getUserData()).interactiveObjectHit((Player) fixA.getUserData());
                } else {
                    ((InteractiveTileObject) fixA.getUserData()).interactiveObjectHit((Player) fixB.getUserData());
                }
                break;

            case NewGame.FIREBALL_BIT | NewGame.ENEMY_BIT:
            case NewGame.PUNCH_BIT | NewGame.ENEMY_BIT :
                if (fixA.getFilterData().categoryBits == NewGame.ENEMY_BIT) {
                    ((Enemy) fixA.getUserData()).punched();
                }
                else {
                    ((Enemy) fixB.getUserData()).punched();
                }

                break;

            case NewGame.ENEMY_BIT | NewGame.WALL_BIT:
                if (fixA.getFilterData().categoryBits == NewGame.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;

            case NewGame.ENEMY_BIT | NewGame.ENEMY_BIT:
                ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;

            case NewGame.ITEM_BIT | NewGame.PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == NewGame.ITEM_BIT)
                    ((Item) fixA.getUserData()).use((Player) fixB.getUserData());
                else
                    ((Item) fixB.getUserData()).use((Player) fixA.getUserData());
                break;

            case NewGame.PLAYER_BIT | NewGame.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == NewGame.PLAYER_BIT) {
                    ((Enemy) fixB.getUserData()).mBeginContact();
                }
                else {
                    ((Enemy) fixA.getUserData()).mBeginContact();
                }
                break;

        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case NewGame.PLAYER_BIT | NewGame.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == NewGame.PLAYER_BIT) {
                    ((Enemy) fixB.getUserData()).mEndContact();
                }
                else {
                    ((Enemy) fixA.getUserData()).mEndContact();
                }
                break;

        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }


}
