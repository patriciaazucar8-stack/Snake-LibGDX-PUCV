package com.mich.snake;

public class Obstaculo extends ObjetoMapa {

    public Obstaculo(int x, int y) {
        super(x, y);
    }

    @Override
    public void alColisionar(PantallaJuego juego) {
        // Lógica polimórfica: Si chocas un obstáculo, mueres
        juego.reiniciarJuego();
    }
}
