package com.mygdx.game;

public class PowerUpVidas implements PowerUp{

    @Override
    public void deshacerEfecto(Nave nave)
    {
        if(nave.getVidas() > 3)
        {
            nave.setVidas(nave.getVidas() - 1);
        }
    }
    @Override
    public void aplicarEfecto(Nave nave) {
        nave.setVidas(nave.getVidas() + 1);
    }
}
