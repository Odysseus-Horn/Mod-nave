package com.mygdx.game;

public class PowerUpVidas extends PowerUp{

    

    public void deshacerEfecto(Nave4 nave)
    {
                
    }
    @Override
    public void aplicarEfecto(Nave4 nave) {
        nave.setVidas(nave.getVidas() + 1);
    }
}
