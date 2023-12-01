package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.LevelPlanner.BombaMarina;


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
    private int scoreSum;
    protected  estrategiaDisparo disparo;

    private Vector2 direccionNave = new Vector2(0, 1);

    private AudioManager audioManager;

    public Nave(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        this.scoreSum = 0;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        //spr.setOriginCenter();
        spr.setBounds(x, y, 45, 45);
        spr.setOrigin(spr.getWidth() / 2, spr.getHeight() / 2);
        audioManager = audioManager.getInstance();
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
    //hay que cambiar esto 
    public void disparar(PantallaJuego juego) {
        float x = spr.getX() + spr.getWidth() / 2 - 5;
        float y = spr.getY() + spr.getHeight() / 2 - 5;
        int xVel = (int) (10 * direccionNave.x);
        int yVel = (int) (10 * direccionNave.y);
        disparo.disparar(x, y, xVel, yVel,this.txBala, juego); // Llamada al método de la interfaz estrategiaDisparo
    }



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
            disparar(juego);
        }

    }

    public boolean checkCollision(BombaMarina b) {
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
            // despegar sprite
            /*
            int cont = 0;
            while (b.getArea().overlaps(spr.getBoundingRectangle()) && cont<xVel) {
               spr.setX(spr.getX()+Math.signum(xVel));
            }   */
            //actualizar vidas y herir

            b.quitarVida(this);

            herido = true;
            tiempoHerido=tiempoHeridoMax;
            audioManager.playSound(sonidoHerido);

            if (vidas<=0)
                destruida = true;
            return true;
        }
        return false;
    }
    public boolean checkCollisionPowerUp(PowerUpBall powerUp, String color) {
        if (!herido && powerUp.getArea().overlaps(spr.getBoundingRectangle())) {
            // Lógica de colisión con power-up basada en color
            switch (color.toLowerCase()) {
                case "bolaroja1":
                    powerUp.aplicarEfectoVidas(this);  // Ejemplo: Aplicar efecto de vidas para power-up rojo
                    return true;
                case "bolazul":
                    powerUp.aplicarEfectoVel(this);
                	return true;
                case "bolanegra1":
                	powerUp.aplicarEfectoPuntos(this);
                    
                	return true;
                case "bolaamarilla":
                    
                	return true;
                default:
                    
                    break;
            }
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
    //el uly me la mama
    public void aumentarVidas(int cantidad) {
        vidas += cantidad;

    }
    public void quitarVida(int cantidad)
    {
        vidas-=cantidad;
    }
    
    public void sumScore(int cantidad) {
    	scoreSum += cantidad;
    }
    
    public int getScore() {return scoreSum; }
    public int getVidas() {return vidas;}
    public void setScore(int score) {scoreSum = score;}
    //public boolean isDestruida() {return destruida;}
    public int getX() {return (int) spr.getX();}
    public int getY() {return (int) spr.getY();}
    public void setVidas(int vidas2) {vidas = vidas2;}
    


    public Vector2 getDireccionNave() {
        return direccionNave;
    }
}
