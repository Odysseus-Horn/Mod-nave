package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public abstract class Nave {
    private boolean destruida = false;
    private int vidas;
    private float xVel = 0;
    private float yVel = 0;
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax=50;
    private int tiempoHerido;

    public Nave(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        //spr.setOriginCenter();
        spr.setBounds(x, y, 45, 45);
    }

    public void draw(SpriteBatch batch, PantallaJuego juego){
        float x =  spr.getX();
        float y =  spr.getY();
        if (!herido) {
            // que se mueva con teclado
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            {
                xVel = -5;
            } else{
                xVel = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            {
                xVel = +5;
            } else{
                xVel = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP))
            {
                yVel = 5;
            } else{
                yVel = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            {
                yVel = -5;
            } else{
                yVel = 0;
            }

	     /*   if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) spr.setRotation(++rotacion);
	        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) spr.setRotation(--rotacion);

	        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
	        	xVel -=Math.sin(Math.toRadians(rotacion));
	        	yVel +=Math.cos(Math.toRadians(rotacion));
	        	System.out.println(rotacion+" - "+Math.sin(Math.toRadians(rotacion))+" - "+Math.cos(Math.toRadians(rotacion))) ;
	        }
	        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
	        	xVel +=Math.sin(Math.toRadians(rotacion));
	        	yVel -=Math.cos(Math.toRadians(rotacion));

	        }*/

            // que se mantenga dentro de los bordes de la ventana
            if (x+xVel < 0 || x+xVel+spr.getWidth() > Gdx.graphics.getWidth())
                xVel*=-1;
            if (y+yVel < 0 || y+yVel+spr.getHeight() > Gdx.graphics.getHeight())
                yVel*=-1;

            spr.setPosition(x+xVel, y+yVel);

            spr.draw(batch);
        } else {
            spr.setX(spr.getX()+ MathUtils.random(-2,2));
            spr.draw(batch);
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido<=0) herido = false;
        }
        // disparo
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Bullet  bala = new Bullet(spr.getX()+spr.getWidth()/2-5,spr.getY()+ spr.getHeight()-5,0,3,txBala);
            juego.agregarBala(bala);
            soundBala.play();
        }
    }
}
