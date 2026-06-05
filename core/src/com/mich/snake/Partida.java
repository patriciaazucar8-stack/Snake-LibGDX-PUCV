package com.mich.snake;

import com.mich.snake.strategies.SkinStrategy;

public class Partida {
    private final int velocidad;
    private final String dificultadNombre; 
    private final SkinStrategy skinMascota;

    Partida(int velocidad, String dificultadNombre, SkinStrategy skinMascota) {
        this.velocidad = velocidad;
        this.dificultadNombre = dificultadNombre;
        this.skinMascota = skinMascota;
    }

    public int getVelocidad() { return velocidad; }
    public String getDificultadNombre() { return dificultadNombre; }
    public SkinStrategy getSkinMascota() { return skinMascota; }
}
