package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


/*clase abstracta con el propósito de modificar diversos tipos de disparo para distintas naves*/

public abstract class Nave {

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

    public Nave(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        //spr.setOriginCenter();
        spr.setBounds(x, y, 45, 45);
        spr.setOrigin(spr.getWidth() / 2, spr.getHeight() / 2);

    }

    private void moverse() {

        // Movimiento y rotación basados en el vector de dirección
        xVel = 0;
        yVel = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direccionNave.set(-1, 0); // Izquierda
            xVel = -5;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direccionNave.set(1, 0); // Derecha
            xVel = 5;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direccionNave.set(0, 1); // Arriba
            yVel = 5;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direccionNave.set(0, -1); // Abajo
            yVel = -5;
        }

        // Combinar las teclas para las diagonales
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direccionNave.set(-1, 1); // Arriba izquierda
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direccionNave.set(1, 1); // Arriba derecha
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direccionNave.set(-1, -1); // Abajo izquierda
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direccionNave.set(1, -1); // Abajo derecha
        }

        // Normalizar el vector de dirección para mantenerlo unitario
        direccionNave.nor();

        // Actualizar el ángulo de rotación en función del vector de dirección
        float degrees = direccionNave.angleDeg();
        spr.setRotation(degrees - 90);
    }

    public abstract void disparar(PantallaJuego juego);

    public void draw(SpriteBatch batch, PantallaJuego juego){
        //se obtienen las dimensiones del sprite
        float x =  spr.getX();
        float y =  spr.getY();
        if (!herido) {
            // que se mueva con teclado
            moverse();

            // que se mantenga dentro de los bordes de la ventana
            if (x+xVel < 0 || x+xVel+spr.getWidth() > Gdx.graphics.getWidth())
                xVel*=-1;
            if (y+yVel < 0 || y+yVel+spr.getHeight() > Gdx.graphics.getHeight())
                yVel*=-1;

            spr.setPosition(x+xVel, y+yVel);

            spr.draw(batch);
        } else {
            spr.setX(spr.getX()+MathUtils.random(-2,2));
            spr.draw(batch);
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido<=0) herido = false;
        }
        // disparo
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Bullet  bala = new Bullet(spr.getX()+spr.getWidth()/2-5,spr.getY()+ spr.getHeight()-5,(int) (10*direccionNave.x),(int) (10*direccionNave.y),txBala);
            juego.agregarBala(bala);
            soundBala.play();
        }

    }

    public boolean checkCollision(Ball2 b) {
        if(!herido && b.getArea().overlaps(spr.getBoundingRectangle())){
            // rebote
            if (xVel ==0) xVel += b.getXSpeed()/2;
            if (b.getXSpeed() ==0) b.setXSpeed(b.getXSpeed() + (int)xVel/2);
            xVel = - xVel;
            b.setXSpeed(-b.getXSpeed());

            if (yVel ==0) yVel += b.getySpeed()/2;
            if (b.getySpeed() ==0) b.setySpeed(b.getySpeed() + (int)yVel/2);
            yVel = - yVel;
            b.setySpeed(- b.getySpeed());
            // despegar sprites
      /*      int cont = 0;
            while (b.getArea().overlaps(spr.getBoundingRectangle()) && cont<xVel) {
               spr.setX(spr.getX()+Math.signum(xVel));
            }   */
            //actualizar vidas y herir
            vidas--;
            herido = true;
            tiempoHerido=tiempoHeridoMax;
            sonidoHerido.play();
            if (vidas<=0)
                destruida = true;
            return true;
        }
        return false;
    }

    public boolean estaDestruido() {
        return !herido && destruida;
    }
    public boolean estaHerido() {
        return herido;
    }

    public void aumentarVelocidad(float porcentaje)
    {
        xVel = (porcentaje/100) + 5*direccionNave.x;
        yVel = (porcentaje/100) + 5*direccionNave.y;
    }

    public int getVidas() {return vidas;}
    //public boolean isDestruida() {return destruida;}
    public int getX() {return (int) spr.getX();}
    public int getY() {return (int) spr.getY();}
    public void setVidas(int vidas2) {vidas = vidas2;}

    public Vector2 getDireccionNave() {
        return direccionNave;
    }
}
