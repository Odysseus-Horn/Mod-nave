package com.mygdx.game.LevelPlanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Nave;


public interface BombaMarina {

    public void update();
    public Rectangle getArea();

    public void checkCollision(BombaMarina b2);
    public void draw(SpriteBatch batch);

    public int getXSpeed();
    public void setXSpeed(int xSpeed);
    public int getySpeed();
    public void setySpeed(int ySpeed);
    public void quitarVida(Nave nave);

    public Sprite getSpr();
}
