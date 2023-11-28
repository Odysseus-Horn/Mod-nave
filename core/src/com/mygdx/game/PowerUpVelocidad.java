package com.mygdx.game;

public class PowerUpVelocidad implements PowerUp{

    @Override
    public void aplicarEfecto(Nave nave) {
        nave.aumentarVelocidad(0.2f);
    }

    @Override
    public void deshacerEfecto(Nave nave) {
        nave.aumentarVelocidad(-0.2f);
    }
}
