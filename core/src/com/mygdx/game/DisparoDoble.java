package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class DisparoDoble implements  estrategiaDisparo{

    @Override
    public void disparar(float x, float y, int xSpeed, int ySpeed, Texture tx, PantallaJuego juego) {



        Bullet bala = new Bullet(x, y, -xSpeed, -ySpeed, tx);
        Bullet bala1 = new Bullet(x, y, xSpeed, ySpeed, tx);
        juego.agregarBala(bala);
        //soundBala.play();
    }
}
