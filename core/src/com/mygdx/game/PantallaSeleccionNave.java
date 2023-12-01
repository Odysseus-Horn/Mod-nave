package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ScreenUtils;



public class PantallaSeleccionNave implements Screen {
    private SpaceNavigation game;
    private OrthographicCamera camera;
    private Nave nave;
    private Texture imagenNave1, imagenNave2,imagenNave3;
    private int naveSeleccionada = 1;
    //Constructores Para los Tipos de Nave
    public PantallaSeleccionNave(SpaceNavigation game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);

        imagenNave1 = new Texture(Gdx.files.internal("BuqueGuerra.png"));
        imagenNave2 = new Texture(Gdx.files.internal("Barco Pirata.png"));


    }
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        game.getBatch().draw(imagenNave1, 100, 500, 300, 300);
        game.getFont().draw(game.getBatch(), "Nave Tipo 1 - Disparo Default", 100, 450);
        game.getBatch().draw(imagenNave2, 500, 500, 300, 300);
        game.getFont().draw(game.getBatch(), "Nave Tipo 2 - Disparo Doble", 500, 450);
        game.getBatch().end();

        game.getBatch().begin();
        if (naveSeleccionada == 1) {
            game.getFont().draw(game.getBatch(), "> Seleccionar <", 100, 350);
        } else {
            game.getFont().draw(game.getBatch(), "Seleccionar", 100, 350);
        }

        // Dibujar seleccionar para nave 2
        if (naveSeleccionada == 2) {
            game.getFont().draw(game.getBatch(), "> Seleccionar <", 500, 350);
        } else {
            game.getFont().draw(game.getBatch(), "Seleccionar", 500, 350);
        }
        game.getBatch().end();

        //Manejo de entrada para seleccionar nave
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && naveSeleccionada == 1) {
            naveSeleccionada = 2;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && naveSeleccionada == 2) {
            naveSeleccionada = 1;
        }

        // Comprobar si se debe iniciar el juego con la nave seleccionada
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            iniciarJuegoConNave(naveSeleccionada);
        }
    }
    private void iniciarJuegoConNave(int naveSeleccionada) {
        // Lógica para iniciar el juego con la nave específica

        if (naveSeleccionada == 1) {
            nave = new BuqueGuerra(Gdx.graphics.getWidth()/2-50,30,new Texture(Gdx.files.internal("BuqueGuerra.png")),
                    Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
                    new Texture(Gdx.files.internal("Rocket2.png")),
                    Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
            nave.setVidas(3);

        } else if (naveSeleccionada == 2) {
            //Hay que crear Otro Tipo de Nave.

            nave = new BarcoPirata(Gdx.graphics.getWidth()/2-50,30,new Texture(Gdx.files.internal("Barco Pirata.png")),
                    Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
                    new Texture(Gdx.files.internal("Rocket2.png")),
                    Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
            nave.setVidas(3);


        }

        Screen ss = new PantallaJuego(game,nave,1,3,0,1,1,10);
        game.setScreen(ss);
        dispose();
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
    }



}
