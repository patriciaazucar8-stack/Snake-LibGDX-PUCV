package com.mich.snake.strategies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mich.snake.RecursosJuego;

public class PezStrategy implements SkinStrategy {

    @Override
    public void dibujarCuerpo(SpriteBatch batch, Vector2 pos, float tam, boolean esCabeza) {
        if (esCabeza) {
            // Pez tamaño Godzilla centrado
            float offset = (40 - tam) / 2f;
            batch.draw(RecursosJuego.getInstance().texPez, pos.x * tam - offset, pos.y * tam - offset, 40, 40);
        } else {
            // El cuerpo va recolectando algas marinas pequeñas (16px)
            batch.draw(RecursosJuego.getInstance().texConcha, pos.x * tam + 2, pos.y * tam + 2, 16, 16);
        }
    }

    @Override
    public void dibujarComida(SpriteBatch batch, Vector2 pos, float tam) {
        // La comida flotante en el mapa es un alga
        batch.draw(RecursosJuego.getInstance().texConcha, pos.x * tam, pos.y * tam, tam, tam);
    }

    @Override
    public Color getColorObstaculos() {
        // Piedras gris oscuro/volcánicas de acuario
        return new Color(0.2f, 0.25f, 0.3f, 1);
    }

    @Override
    public Color getColorBordes() {
        // Un azul calipso/neón hermoso para simular el agua/pecera
        return new Color(0.0f, 0.7f, 0.9f, 1);
    }
    @Override
    public void dibujarFondo(SpriteBatch batch, int anchoPantalla, int altoPantalla, int tamCelda) {
        int celdasX = anchoPantalla / tamCelda;
        int celdasY = altoPantalla / tamCelda;

        for (int x = 1; x < celdasX - 1; x += 2) {
            for (int y = 1; y < celdasY - 1; y += 2) {
                if (((x / 2) + (y / 2)) % 2 == 0) {
                    batch.setColor(0.2f, 0.6f, 0.9f, 1f); // Azul agua claro
                } else {
                    batch.setColor(0.1f, 0.4f, 0.7f, 1f); // Azul oceánico oscuro
                }
                // Puedes usar texPasto o un texAgua si lo agregas al Singleton
                batch.draw(RecursosJuego.getInstance().texPasto, x * tamCelda, y * tamCelda, tamCelda * 4, tamCelda * 4);
            }
        }
        batch.setColor(com.badlogic.gdx.graphics.Color.WHITE);
    }
    @Override
    public String getNombreComida() { return "Conchas"; }
}
