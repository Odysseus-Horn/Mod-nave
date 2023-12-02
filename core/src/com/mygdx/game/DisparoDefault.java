package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;



public class DisparoDefault implements estrategiaDisparo{

    private Sound soundBala;

    // Constructor que acepta un Sound como argumento
    public DisparoDefault(Sound soundBala) {
        this.soundBala = soundBala;
    }
    //Estrategia Del disparo Default
    public void disparar(float x, float y, int xSpeed, int ySpeed, Texture tx, PantallaJuego juego){
        Bullet bala = new Bullet(x, y, xSpeed, ySpeed, tx);
        juego.agregarBala(bala);
    }

}