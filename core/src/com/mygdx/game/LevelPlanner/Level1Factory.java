package com.mygdx.game.LevelPlanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.Random;

// clase concreta para una fábrica de nivel 1
public class Level1Factory implements LevelFactory{

    public Level1Factory(){

    }

    //crea una cantidad dada por parámetro de cierto obstáculo a crear
    @Override
    public ArrayList<BombaMarina> crearObstaculos(int cant, int velX, int velY) {
        ArrayList obstaculosNivel1 = new ArrayList<BombaMarina>();

        Random r = new Random();
        for (int i = 0; i < cant; i++) {
            BombaMarinaLevel1 bb = new BombaMarinaLevel1(r.nextInt((int) Gdx.graphics.getWidth()),
                    50+r.nextInt((int)Gdx.graphics.getHeight()-50),
                    20+r.nextInt(10), velX +r.nextInt(4), velY+r.nextInt(4),
                    new Texture(Gdx.files.internal("sea-mine.png")));
            obstaculosNivel1.add(bb);

        }
        return obstaculosNivel1;
    }
}
