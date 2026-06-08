package com.mich.snake;

public class Comida extends ObjetoMapa {

    public Comida(int x, int y) {
        super(x, y);
    }

    @Override
    protected void ejecutarAccionEspecifica(PantallaJuego juego) {
        juego.puntosActuales++;
        
        juego.esperandoSiguienteNivel = false;
    }

    public void setPosicion(int x, int y) {
        this.posicion.x = x;
        this.posicion.y = y;
    }
}