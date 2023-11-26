package com.mygdx.game;
import com.badlogic.gdx.graphics.Texture;

//metodo Strattegy
public interface estrategiaDisparo {
    public void disparar(float x, float y, int xSpeed, int ySpeed, Texture tx, PantallaJuego juego);

}
