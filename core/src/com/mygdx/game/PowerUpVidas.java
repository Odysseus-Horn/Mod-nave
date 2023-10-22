package com.mygdx.game;

public class PowerUpVidas implements PowerUp{

    

    @Override
    public void deshacerEfecto(Nave4 nave)
    {
        if(nave.getVidas() > 3)
        {
            nave.setVidas(nave.getVidas() - 1);
        }
    }
    @Override
    public void aplicarEfecto(Nave4 nave) {
        nave.setVidas(nave.getVidas() + 1);
    }
}
