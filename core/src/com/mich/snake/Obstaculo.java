package com.mich.snake;

public class Obstaculo extends ObjetoMapa {

    public Obstaculo(int x, int y) {
        super(x, y);
    }

    @Override
    protected void ejecutarAccionEspecifica(PantallaJuego juego) {
        juego.activarGameOver();
    }
}
