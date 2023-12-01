package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

//clase abstracta base del template method
public abstract class PowerUpTemplate {
	public abstract void aplicarEfectoVidas(Nave nave);
    public abstract void deshacerEfectoVidas(Nave nave);
    public abstract void aplicarEfectoVel(Nave nave);
    public abstract void deshacerEfectoVel(Nave nave);
    public abstract void aplicarEfectoPuntos(Nave nave);
    //public abstract void deshacerEfectoQuitar(Nave nave);
    //public abstract void aplicarEfectoVel(Nave nave);
    //public abstract void deshacerEfectoVel(Nave nave);

    public void draw(SpriteBatch batch, PantallaJuego juego, Nave nave) {
        nave.draw(batch, juego);
    }
}
