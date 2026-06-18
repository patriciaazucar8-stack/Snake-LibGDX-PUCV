package com.mich.snake.strategies;

public class VelocidadBase implements VelocidadStrategy {
	@Override
    public float modificarVelocidad(float velocidadBase) {
        return velocidadBase; // Velocidad Media
    }
}