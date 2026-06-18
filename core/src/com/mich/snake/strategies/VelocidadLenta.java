package com.mich.snake.strategies;

public class VelocidadLenta implements VelocidadStrategy {
	@Override
    public float modificarVelocidad(float velocidadBase) {
        return 0.16f; // Forzamos la velocidad lenta exacta del modo Fácil
    }
}