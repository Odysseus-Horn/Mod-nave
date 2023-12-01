package com.mygdx.game.LevelPlanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.Random;

public class Level2Factory implements LevelFactory{
    public Level2Factory(){

    }

    //crea una cantidad de obstáculos para un objeto concreto
    @Override
    public ArrayList<BombaMarina> crearObstaculos(int cant, int velX, int velY) {
        ArrayList obstaculosNivel2 = new ArrayList<BombaMarina>();

        Random r = new Random();
        for (int i = 0; i < cant; i++) {
            BombaMarinaLevel2 bb = new BombaMarinaLevel2(r.nextInt((int) Gdx.graphics.getWidth()),
                    50+r.nextInt((int)Gdx.graphics.getHeight()-50),
                    20+r.nextInt(10), velX +r.nextInt(4), velY+r.nextInt(4),
                    new Texture(Gdx.files.internal("sea-mine-2.png")));
            obstaculosNivel2.add(bb);

        }
        return obstaculosNivel2;
    }
}
