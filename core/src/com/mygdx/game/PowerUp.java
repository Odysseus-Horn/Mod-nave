package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class PowerUp {
    protected Sprite spr;
    protected int x;
    protected int y;
    int duracion;

    protected abstract void deshacerEfecto(Nave4 nave);

    public abstract void aplicarEfecto(Nave4 nave);
}

