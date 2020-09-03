package com.abiyedanagogo.game.sprites.enemies;

import com.abiyedanagogo.game.NewGame;
import com.abiyedanagogo.game.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

/*
 * Created by Abiye Danagogo on 23/07/2020
 * */
public class Flyer extends Enemy {

    private enum State {MOVING, ATTACKING}
    private State currentState;
    private State previousState;

    private float stateTime;
    private Animation<TextureRegion> moveAnimation;
    private Array<TextureRegion> frames;
    private Texture texture;

    private TextureRegion attack;

    private float previousPosition;
    private float currentPosition;
    private int positionTimer;

    public Flyer(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        texture = new Texture("flyingmonster.png");
        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(texture, i * 320, 0, 320, 320));
        }

        moveAnimation = new Animation<TextureRegion>(0.2f, frames);

        attack = new TextureRegion(texture, 320, 0, 320, 320);

        currentState = previousState = State.MOVING;

        setBounds(getX(), getY(), 72 / NewGame.PPM, 72 / NewGame.PPM);

        currentPosition = previousPosition = b2body.getPosition().x;
        positionTimer = 0;

    }

    @Override
    public void update(float dt) {
        stateTime += dt;
        if (setToDestroy && !destroyed) {

            world.destroyBody(b2body);
            b2body = null;

            destroyed = true;
            setRegion(new TextureRegion(texture, 0, 0, 320, 320));
            stateTime = 0;
        }
        else if (!destroyed) {
            setRegion(getFrame(dt));
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            if (!detected) {
                b2body.setLinearVelocity(velocity);
            }

            else {
                if (screen.getPlayer().getB2body().getPosition().x < b2body.getPosition().x) {
                    b2body.setLinearVelocity(-1, 0);
                }
                else if (screen.getPlayer().getB2body().getPosition().x > b2body.getPosition().x) {
                    b2body.setLinearVelocity(1, 0);
                }

                sprite.setPosition(getX() + (getWidth() / 2), getY() + getHeight());
            }

            if (contact) {
                if (stateTime > 2) {
                    screen.getPlayer().hit(this);
                    currentState = State.ATTACKING;
                    stateTime = 0;
                }
            }

            previousPosition = currentPosition;
            currentPosition = b2body.getPosition().x;


            if (currentPosition == previousPosition && b2body.isActive())
                positionTimer += 1;
            else if (currentPosition != previousPosition && b2body.isActive())
                positionTimer = 0;

            if (positionTimer > 300)
                reverseVelocity(true, false);


            if (currentState == State.ATTACKING && stateTime > 1) {
                currentState = State.MOVING;
            }

        }

    }



    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        switch (currentState) {
            case ATTACKING:
                region = attack;
                break;
            case MOVING:
            default:
                region = moveAnimation.getKeyFrame(stateTime, true);
                break;
        }

        if (b2body.getLinearVelocity().x > 0 && !region.isFlipX()) {
            region.flip(true, false);
        }

        else if (b2body.getLinearVelocity().x < 0 && region.isFlipX()) {
            region.flip(true, false);
        }

        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return region;
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(28 / NewGame.PPM, 28 / NewGame.PPM);

        //CircleShape shape = new CircleShape();
        //shape.setRadius(28 / NewGame.PPM);

        fdef.filter.categoryBits = NewGame.ENEMY_BIT;
        fdef.filter.maskBits = NewGame.GROUND_BIT |
                NewGame.WALL_BIT |
                NewGame.ENEMY_BIT |
                NewGame.PLAYER_BIT |
                NewGame.PUNCH_BIT |
                NewGame.FIREBALL_BIT;

        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1)
            super.draw(batch);
    }

    @Override
    public void punched() {
        setToDestroy = true;
    }
    @Override
    public void mBeginContact() {
        screen.getPlayer().hit(this);
        contact = true;
        if (currentState != State.ATTACKING)
            currentState = State.ATTACKING;
    }

    @Override
    public void mEndContact() {
        contact = false;
    }
}
