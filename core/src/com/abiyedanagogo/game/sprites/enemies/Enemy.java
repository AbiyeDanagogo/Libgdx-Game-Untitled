package com.abiyedanagogo.game.sprites.enemies;

import com.abiyedanagogo.game.NewGame;
import com.abiyedanagogo.game.screens.PlayScreen;
import com.abiyedanagogo.game.sprites.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    protected Body b2body;
    protected Vector2 velocity;

    protected boolean setToDestroy;
    protected boolean destroyed;
    protected boolean detected;
    protected boolean contact;

    protected Sprite sprite;

    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(0.2f, 0);
        b2body.setActive(false);

        setToDestroy = false;
        destroyed = false;
        detected = false;
        contact = false;


        sprite = new Sprite(new Texture("exclamation.png"));
        sprite.setBounds(getX(), getY(), 15 / NewGame.PPM, 40 / NewGame.PPM);
    }

    protected abstract void defineEnemy();
    public abstract void punched();
    public abstract void update(float dt);
    public abstract void mBeginContact();
    public abstract void mEndContact();

    public void reverseVelocity(boolean x, boolean y) {
        if(x)
            velocity.x = -velocity.x;
        if (y)
            velocity.y = -velocity.y;
    }

    public void draw(Batch batch) {
        super.draw(batch);
        if (detected && !destroyed)
            sprite.draw(batch);
    }

    public Body getB2body() {
        return b2body;
    }

    public boolean isDetected() {
        return detected;
    }

    public void setDetected(boolean detected) {
        this.detected = detected;
    }
}
