package com.mygdx.game.LevelPlanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Ball2;

import java.util.ArrayList;
import java.util.Random;

public class Level1Factory implements LevelFactory{

    public Level1Factory(){

    }
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
