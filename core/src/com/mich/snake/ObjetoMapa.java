package com.mich.snake;

import com.badlogic.gdx.math.Vector2;

// LA CLASE PADRE 
public abstract class ObjetoMapa {
    // Encapsulado como protected para que sus hijos lo hereden directamente
    protected Vector2 posicion;

    public ObjetoMapa(int x, int y) {
        this.posicion = new Vector2(x, y);
    }

    public Vector2 getPosicion() {
        return posicion;
    }

    // Cada objeto define su propia consecuencia al ser tocado
    public abstract void alColisionar(PantallaJuego juego);
}