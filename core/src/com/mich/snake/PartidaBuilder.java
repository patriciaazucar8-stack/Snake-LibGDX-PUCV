package com.mich.snake;

import com.mich.snake.strategies.SkinStrategy;

public class PartidaBuilder {
    private int velocidad = 120; // Por defecto Medio
    private String dificultadNombre = "MEDIO";
    private SkinStrategy skinMascota;

    public PartidaBuilder() {}

    public PartidaBuilder conDificultad(String dificultad) {
        if (dificultad.equalsIgnoreCase("FACIL")) {
            this.velocidad = 160; // Se mueve mas lento
            this.dificultadNombre = "FACIL";
        } else if (dificultad.equalsIgnoreCase("DIFICIL")) {
            this.velocidad = 80;  // Se mueve mas rapido
            this.dificultadNombre = "DIFICIL";
        } else {
            this.velocidad = 120;
            this.dificultadNombre = "MEDIO";
        }
        return this;
    }

    public PartidaBuilder conMascota(SkinStrategy skinMascota) {
        this.skinMascota = skinMascota;
        return this;
    }
    
    public Partida build() {
        return new Partida(velocidad, dificultadNombre, skinMascota);
    }
}