package com.mich.snake;

public class Comida extends ObjetoMapa {

    public Comida(int x, int y) {
        super(x, y); // Pasa las coordenadas al constructor padre
    }

    @Override
    public void alColisionar(PantallaJuego juego) {
        // Si la mascota come, suma puntos y reubica la comida
        juego.puntosActuales++;
        juego.spawnComida();
        
        if (juego.puntosActuales >= juego.metaComida) {
            juego.esperandoSiguienteNivel = true;
        }
    }

    // Método para moverla cuando spawnea en otro lado
    public void setPosicion(int x, int y) {
        this.posicion.set(x, y);
    }
}