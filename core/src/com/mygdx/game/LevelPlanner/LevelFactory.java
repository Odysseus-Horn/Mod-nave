package com.mygdx.game.LevelPlanner;

import java.util.ArrayList;

public interface LevelFactory {
    public ArrayList<BombaMarina> crearObstaculos(int cant, int velX, int velY);
}
