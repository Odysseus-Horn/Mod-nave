package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

public interface PowerUp {
    public  int cantPowerUps = 3;
    public void deshacerEfecto(Nave nave);

    public  void aplicarEfecto(Nave nave);


}

