package com.abiyedanagogo.game.sprites;

import com.abiyedanagogo.game.NewGame;
import com.abiyedanagogo.game.screens.PlayScreen;
import com.abiyedanagogo.game.sprites.enemies.Enemy;
import com.abiyedanagogo.game.tools.Hud;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/*
 * Created by Abiye Danagogo on 29/06/2020
 * */
public class Player extends Sprite {

    private enum State { FALLING, JUMPING, STANDING, RUNNING, ATTACKING, HIT};
    private State currentState;
    private State previousState;

    private Animation<TextureRegion> playerRun;
    private Animation<TextureRegion> playerJump;
    private Animation<TextureRegion> playerAttackAnimation;
    private Animation<TextureRegion> playerHitAnimation;

    private boolean runningRight;

    private PlayScreen screen;
    private World world;
    private Body b2body;
    private Hud hud;

    private TextureRegion playerStand;
    private TextureAtlas atlas;

    private float stateTime;

    private boolean playerAttacking;
    private boolean finishAttack;

    private boolean playerHit;

    private Array<FireBall> fireBalls;

    private int fireBallCounter;

    private float hitSpeed;

    public Player(PlayScreen screen) {
        this.screen = screen;
        this.world = screen.getWorld();
        this.hud = screen.getHud();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTime = 0;
        runningRight = true;

        playerAttacking = false;
        finishAttack = false;

        playerHit = false;

        hitSpeed = 2;

        atlas = new TextureAtlas("sprites/player.pack");

        playerStand = new TextureRegion(atlas.findRegion("attack"), 0,0, 540, 540);

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 10; i++) {
            frames.add(new TextureRegion(atlas.findRegion("run"), i * 540, 0, 540, 540));
        }
        playerRun = new Animation<TextureRegion>(0.05f, frames);
        frames.clear();

        for (int i = 0; i < 10; i++) {
            frames.add(new TextureRegion(atlas.findRegion("jump"), i * 540, 0, 540, 540));
        }
        playerJump = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();


        for (int i = 1; i < 10; i++) {
            frames.add(new TextureRegion(atlas.findRegion("attack"), i * 540, 0, 540, 540));
        }
        playerAttackAnimation = new Animation<TextureRegion>(0.02f, frames);
        frames.clear();

        for (int i = 1; i < 3; i++) {
            frames.add(new TextureRegion(atlas.findRegion("die"), i * 540, 0, 540, 540));
        }
        playerHitAnimation = new Animation<TextureRegion>(0.5f, frames);


        setBounds(0, 0, 70 / NewGame.PPM, 70 / NewGame.PPM);
        setRegion(playerStand);


        definePlayer();

        fireBalls = new Array<FireBall>();
        fireBallCounter = 4;
        hud.fireballCounter(fireBallCounter);
    }

    public void update(float dt) {

        if (runningRight)
            setPosition((b2body.getPosition().x - getWidth() / 2) + 10 / NewGame.PPM, (b2body.getPosition().y - getHeight() / 2));
        else
            setPosition((b2body.getPosition().x - getWidth() / 2) - 10 / NewGame.PPM, (b2body.getPosition().y - getHeight() / 2));
        setRegion(getFrame(dt));

        if (playerAttacking) {
            attack();
        }

        if (finishAttack && stateTime > 0.6f) {
            redefinePlayer();
        }


        for (FireBall ball : fireBalls) {
            ball.update(dt);
            if (ball.isDestroyed())
                fireBalls.removeValue(ball, true);
        }

        if (playerHit) {
            b2body.setLinearVelocity(hitSpeed, b2body.getLinearVelocity().y);
            if (stateTime > 0.5f)
                playerHit = false;
        }
    }

    public TextureRegion getFrame(float dt) {
        currentState  = getState();

        TextureRegion region;

        switch (currentState) {
            case ATTACKING:
                region = playerAttackAnimation.getKeyFrame(stateTime);
                break;
            case JUMPING:
                region = playerJump.getKeyFrame(stateTime);
                break;
            case RUNNING:
                region = playerRun.getKeyFrame(stateTime, true);
                break;
            case HIT:
                region = playerHitAnimation.getKeyFrame(stateTime);
                break;
            case FALLING:
            case STANDING:
            default:
                region = playerStand;
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }
        else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }


        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {

        if (playerHit)
            return State.HIT;
        else if (playerAttacking)
            return State.ATTACKING;
        else if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(100 / NewGame.PPM, 100 / NewGame.PPM);
        //bdef.position.set(4700 / NewGame.PPM, 100 / NewGame.PPM);

        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(5 / NewGame.PPM, 28 / NewGame.PPM);

        fdef.filter.categoryBits = NewGame.PLAYER_BIT;
        fdef.filter.maskBits = NewGame.GROUND_BIT |
                NewGame.LIFE_BIT |
                NewGame.GREEN_ORB_BIT |
                NewGame.WALL_BIT|
                NewGame.ENEMY_BIT |
                NewGame.BOX_BIT |
                NewGame.ITEM_BIT;

        //fdef.friction = 0;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);


    }

    public void attack() {
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(5 / NewGame.PPM, 28 / NewGame.PPM);
        fdef.shape = shape;
        b2body.createFixture(fdef);

        PolygonShape fist = new PolygonShape();
        Vector2[] vertice = new Vector2[4];


        vertice[0] = new Vector2(0, 28).scl(1 / NewGame.PPM);
        vertice[2] = new Vector2(0, -28).scl(1 / NewGame.PPM);

        if (runningRight) {
            vertice[1] = new Vector2(40, 28).scl(1 / NewGame.PPM);
            vertice[3] = new Vector2(40, -28).scl(1 / NewGame.PPM);
        } else {
            vertice[1] = new Vector2(-40, 28).scl(1 / NewGame.PPM);
            vertice[3] = new Vector2(-40, -28).scl(1 / NewGame.PPM);
        }

        fist.set(vertice);

        fdef.filter.categoryBits = NewGame.ATTACK_BIT;
        fdef.filter.maskBits = NewGame.GROUND_BIT |
                NewGame.LIFE_BIT |
                NewGame.GREEN_ORB_BIT |
                NewGame.WALL_BIT|
                NewGame.ENEMY_BIT |
                NewGame.BOX_BIT;

        fdef.shape = fist;
        b2body.createFixture(fdef).setUserData(this);
        
        finishAttack = true;
    }

    public void redefinePlayer() {
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(5 / NewGame.PPM, 28 / NewGame.PPM);

        fdef.filter.categoryBits = NewGame.PLAYER_BIT;
        fdef.filter.maskBits = NewGame.GROUND_BIT |
                NewGame.LIFE_BIT |
                NewGame.GREEN_ORB_BIT |
                NewGame.WALL_BIT |
                NewGame.ENEMY_BIT |
                NewGame.BOX_BIT |
                NewGame.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        finishAttack = false;

        playerAttacking = false;


    }


    public void fire() {
        if (fireBallCounter > 0) {
            fireBalls.add(new FireBall(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight));
            fireBallCounter -= 1;
        }

        if (fireBallCounter < 0)
            fireBallCounter = 0;

        hud.fireballCounter(fireBallCounter);
    }

    public void draw(Batch batch) {
        super.draw(batch);
        for(FireBall ball : fireBalls)
            ball.draw(batch);
    }

    public void addExtraFireBall() {
        fireBallCounter += 4;
        hud.fireballCounter(fireBallCounter);
    }

    public void hit(Enemy enemy) {
        screen.getHud().addLife(-1);
        if (enemy.getX() > this.getX())
            hitSpeed = -2;
        else
            hitSpeed = 2;
        playerHit = true;
    }

    public boolean isPlayerHit() {
        return playerHit;
    }

    public Body getB2body() {
        return b2body;
    }

    public void setPlayerAttacking(boolean playerAttacking) {
        this.playerAttacking = playerAttacking;
    }
}
