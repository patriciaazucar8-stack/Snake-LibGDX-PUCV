package com.mich.snake;

import com.badlogic.gdx.math.Vector2;

public abstract class ObjetoMapa {
    protected Vector2 posicion;

    public ObjetoMapa(int x, int y) {
        this.posicion = new Vector2(x, y);
    }

    public Vector2 getPosicion() {
        return posicion;
    }

    public final void procesarColision(PantallaJuego juego) {
        ejecutarAccionEspecifica(juego);
        
    }

    protected abstract void ejecutarAccionEspecifica(PantallaJuego juego);
}