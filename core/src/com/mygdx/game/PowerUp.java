package com.mygdx.game;

public abstract class PowerUp {

    Sprite spr;
    int x;
    int y;


    protected abstract void deshacerEfecto();

    public abstract void aplicarEfecto(Nave4 nave);
}

