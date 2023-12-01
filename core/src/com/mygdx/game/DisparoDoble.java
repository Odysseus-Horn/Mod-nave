package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class DisparoDoble implements  estrategiaDisparo{

    private Sound soundBala;

    // Constructor que acepta un Sound como argumento
    public DisparoDoble(Sound soundBala) {
        this.soundBala = soundBala;
    }
    @Override
    public void disparar(float x, float y, int xSpeed, int ySpeed, Texture tx, PantallaJuego juego) {
        Bullet bala = new Bullet(x, y, -xSpeed, -ySpeed, tx);
        Bullet bala1 = new Bullet(x, y, xSpeed, ySpeed, tx);
        juego.agregarBala(bala);
        juego.agregarBala(bala1);
        soundBala.play();
    }
}
