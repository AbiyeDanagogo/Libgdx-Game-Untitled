package com.abiyedanagogo.game.items;

import com.abiyedanagogo.game.NewGame;
import com.abiyedanagogo.game.screens.PlayScreen;
import com.abiyedanagogo.game.sprites.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/*
 * Created by Abiye Danagogo on 10/07/2020
 * */
public class ExtraLife extends Item {
    private Texture texture;

    public ExtraLife(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        texture = new Texture("hearticon.png");
        setRegion(texture);
        setBounds(getX(), getY(), 19.8f / NewGame.PPM, 18 / NewGame.PPM);
        velocity = new Vector2(0, 0);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(5 / NewGame.PPM, 5 / NewGame.PPM);

        fdef.filter.categoryBits = NewGame.ITEM_BIT;
        fdef.filter.maskBits = NewGame.PLAYER_BIT |
                                NewGame.BOX_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Player player) {
        destroy();
        screen.getHud().addLife(1);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (body != null){
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            body.setLinearVelocity(velocity);
        }
    }

}
