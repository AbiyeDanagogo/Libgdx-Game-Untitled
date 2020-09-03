package com.abiyedanagogo.game.sprites;

import com.abiyedanagogo.game.NewGame;
import com.abiyedanagogo.game.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/*
 * Created by Abiye Danagogo on 14/07/2020
 * */
public class FireBall extends Sprite {

    PlayScreen screen;
    World world;
    Array<TextureRegion> frames;
    Animation fireAnimation;
    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean fireRight;

    Body b2body;

    TextureRegion fireballTexture;

    public FireBall(PlayScreen screen, float x, float y, boolean fireRight) {
        this.screen = screen;
        this.world = screen.getWorld();
        this.fireRight = fireRight;

        Texture texture = new Texture("fireball.png");
        //fireballTexture = new TextureRegion(texture, 0,0, 682, 535);
        setRegion(texture);
        setBounds(x, y, 20 / NewGame.PPM, 12 / NewGame.PPM);

        if (!fireRight)
            flip(true, false);
        defineFireBall();
    }


    public void update(float dt) {
        stateTime += dt;
        //setRegion();
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if((stateTime > 2 || setToDestroy) && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        //if(b2body.getLinearVelocity().y > 2f)
        //    b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);


        if((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0))
            setToDestroy();
    }


    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }

    public void defineFireBall() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 12 / NewGame.PPM : getX() - 12/NewGame.PPM, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        if (!world.isLocked())
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / NewGame.PPM);
        fdef.filter.categoryBits = NewGame.FIREBALL_BIT;
        fdef.filter.maskBits = NewGame.GROUND_BIT |
                NewGame.ENEMY_BIT |
                NewGame.WALL_BIT;

        fdef.shape = shape;
        fdef.restitution = 1;
        //fdef.friction = 0;
        b2body.setGravityScale(0);

        b2body.createFixture(fdef).setUserData(this);

        b2body.applyLinearImpulse(new Vector2(fireRight ? 4 : -4, 0), b2body.getWorldCenter(), true);

    }

}
