package com.mich.snake.strategies;

public class VelocidadRapida implements VelocidadStrategy {
	@Override
    public float modificarVelocidad(float velocidadBase) {
        return 0.08f; // Forzamos la velocidad turbo exacta del modo Difícil
    }
}
