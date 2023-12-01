package com.mygdx.game.LevelPlanner;

import java.util.ArrayList;

//interfaz para crear assets de un nivel en específico
public interface LevelFactory {
    public ArrayList<BombaMarina> crearObstaculos(int cant, int velX, int velY);
}
