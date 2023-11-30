package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public  class PowerUp   {
	
    public  int cantPowerUps = 2;

    public void draw(SpriteBatch batch, PantallaJuego juego, Nave nave) {
        nave.draw(batch, juego);

    }

    //vel- uly ricaaa
    public void aplicarEfectoVel(Nave nave) {
        nave.aumentarVelocidad(0.2f);
    }

    public void deshacerEfectoVel(Nave nave) {
        nave.aumentarVelocidad(-0.2f);
    }
    //vida uly rico
    public void deshacerEfectoVidas(Nave nave)
    {
        if(nave.getVidas() > 3)
        {
            nave.setVidas(nave.getVidas() - 1);
        }
    }
    public void aplicarEfectoVidas(Nave nave) {
        nave.setVidas(nave.getVidas() + 1);
    }


}

