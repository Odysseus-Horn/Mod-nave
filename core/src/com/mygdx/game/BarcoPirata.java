package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class BarcoPirata extends Nave{
    private boolean destruida = false;
    private int vidas = 3;
    private float xVel = 0;
    private float yVel = 0;
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax=50;
    private int tiempoHerido;

    private Vector2 direccionNave = new Vector2(0, 1);

    public BarcoPirata(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        super(x,y,tx,soundChoque,txBala,soundBala);
        this.disparo = new DisparoDoble(soundBala);
    }


    public boolean estaDestruido() {
        return !herido && destruida;
    }
    public boolean estaHerido() {
        return herido;
    }

    public int getVidas() {return vidas;}
    //public boolean isDestruida() {return destruida;}
    public int getX() {return (int) spr.getX();}
    public int getY() {return (int) spr.getY();}
    public void setVidas(int vidas2) {vidas = vidas2;}
}
