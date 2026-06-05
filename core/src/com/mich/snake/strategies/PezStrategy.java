package com.mich.snake.strategies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mich.snake.RecursosJuego;

public class PezStrategy implements SkinStrategy {

    @Override
    public void dibujarCuerpo(SpriteBatch batch, Vector2 pos, float tam, boolean esCabeza) {
        if (esCabeza) {
            // Imagen (40px)
            float offset = (40 - tam) / 2f;
            batch.draw(RecursosJuego.getInstance().texPez, pos.x * tam - offset, pos.y * tam - offset, 40, 40);
        } else {
            // El cuerpo va recolectando conchitas (16px)
            batch.draw(RecursosJuego.getInstance().texConcha, pos.x * tam + 2, pos.y * tam + 2, 16, 16);
        }
    }

    @Override
    public void dibujarComida(SpriteBatch batch, Vector2 pos, float tam) {
        // Los objetos flotantes son en el mapa conchas rosadas
        batch.draw(RecursosJuego.getInstance().texConcha, pos.x * tam, pos.y * tam, tam, tam);
    }

    @Override
    public Color getColorObstaculos() {
        // Verde para que resalte en el agua
        return new Color(0.12f, 0.65f, 0.35f, 1f); 
    }

    @Override
    public Color getColorBordes() {
        // Azul para simular el agua
        return new Color(0.0f, 0.7f, 0.9f, 1);
    }
    @Override
    public void dibujarFondo(SpriteBatch batch, int anchoPantalla, int altoPantalla, int tamCelda) {
        int celdasX = anchoPantalla / tamCelda;
        int celdasY = altoPantalla / tamCelda;

        for (int x = 1; x < celdasX - 1; x += 2) {
            for (int y = 1; y < celdasY - 1; y += 2) {
                if (((x / 2) + (y / 2)) % 2 == 0) {
                    // Celeste 
                    batch.setColor(0.65f, 0.92f, 1.0f, 1f); 
                } else {
                    // Turquesa para el tablero
                    batch.setColor(0.55f, 0.85f, 0.95f, 1f); 
                }
                batch.draw(RecursosJuego.getInstance().texPasto, x * tamCelda, y * tamCelda, tamCelda * 4, tamCelda * 4);
            }
        }
        batch.setColor(com.badlogic.gdx.graphics.Color.WHITE);
    }
    @Override
    public String getNombreComida() { return "Conchas"; }
}
