package com.mygdx.game.LevelPlanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Nave;

//objetos de bomba utilizados para la ronda 1
public class BombaMarinaLevel1 implements BombaMarina{
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;
    private Sprite spr;

    //constructor
    public BombaMarinaLevel1(int x, int y, int size, int xSpeed, int ySpeed, Texture tx) {
        spr = new Sprite(tx);
        this.x = x;

        //validar que borde de esfera no quede fuera
        if (x-size < 0) this.x = x+size;
        if (x+size > Gdx.graphics.getWidth())this.x = x-size;

        this.y = y;
        //validar que borde de esfera no quede fuera
        if (y-size < 0) this.y = y+size;
        if (y+size > Gdx.graphics.getHeight())this.y = y-size;

        spr.setPosition(x, y);
        this.setXSpeed(xSpeed);
        this.setySpeed(ySpeed);
    }

    //actualiza la posición de la velocidad
    public void update() {
        x += getXSpeed();
        y += getySpeed();

        if (x+getXSpeed() < 0 || x+getXSpeed()+spr.getWidth() > Gdx.graphics.getWidth())
            setXSpeed(getXSpeed() * -1);
        if (y+getySpeed() < 0 || y+getySpeed()+spr.getHeight() > Gdx.graphics.getHeight())
            setySpeed(getySpeed() * -1);
        spr.setPosition(x, y);
    }


    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public void checkCollision(BombaMarina b2) {
        //obtiene las hitboxes de ambas bombas
        Rectangle thisArea = this.getArea();
        Rectangle otherArea = b2.getArea();

        if (thisArea.overlaps(otherArea)) {
            // Intercambiar las direcciones de movimiento para simular el rebote
            int tempXSpeed = this.getXSpeed();
            int tempYSpeed = this.getySpeed();

            this.setXSpeed(b2.getXSpeed());
            this.setySpeed(b2.getySpeed());

            b2.setXSpeed(tempXSpeed);
            b2.setySpeed(tempYSpeed);
        }
    }


    public int getXSpeed() {
        return xSpeed;
    }
    public void setXSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }
    public int getySpeed() {
        return ySpeed;
    }
    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }

    public Sprite getSpr()
    {
        return spr;
    }
    public void quitarVida(Nave nave)
    {
        nave.setVidas(nave.getVidas() - 1);
    }
}
