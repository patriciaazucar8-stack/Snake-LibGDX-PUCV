package com.mich.snake.strategies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mich.snake.RecursosJuego;

public class PerroStrategy implements SkinStrategy {
    @Override
    public void dibujarCuerpo(SpriteBatch batch, Vector2 pos, float tam, boolean esCabeza) {
        if (esCabeza) {
            float offset = (40 - tam) / 2f;
            batch.draw(RecursosJuego.getInstance().texPerro, pos.x * tam - offset, pos.y * tam - offset, 40, 40);
        } else {
            // El perro deja una fila de huesitos flotantes
            batch.draw(RecursosJuego.getInstance().texHueso, pos.x * tam + 2, pos.y * tam + 2, 16, 16);
        }
    }

    @Override
    public void dibujarComida(SpriteBatch batch, Vector2 pos, float tam) {
        batch.draw(RecursosJuego.getInstance().texHueso, pos.x * tam, pos.y * tam, tam, tam);
    }

    @Override
    public Color getColorObstaculos() {
        return new Color(0.5f, 0.25f, 0f, 1); // Piedras color cafe
    }
    @Override
    public Color getColorBordes() {
        return new Color(0.76f, 0.745f, 0.745f, 1); // Bordes grises para que no desentone
    }
    @Override
    public void dibujarFondo(SpriteBatch batch, int anchoPantalla, int altoPantalla, int tamCelda) {
        int celdasX = anchoPantalla / tamCelda;
        int celdasY = altoPantalla / tamCelda;

        for (int x = 1; x < celdasX - 1; x += 2) {
            for (int y = 1; y < celdasY - 1; y += 2) {
                if (((x / 2) + (y / 2)) % 2 == 0) {
                    batch.setColor(0.58f, 1f, 0.368f, 1f); // Verde claro
                } else {
                    batch.setColor(0.50f, 0.90f, 0.30f, 1f); // Verde oscuro
                }
                batch.draw(RecursosJuego.getInstance().texPasto, x * tamCelda, y * tamCelda, tamCelda * 4, tamCelda * 4);
            }
        }
        // Restauramos el color a blanco puro para no teñir el HUD ni los personajes
        batch.setColor(com.badlogic.gdx.graphics.Color.WHITE);
    }
    @Override
    public String getNombreComida() { return "Huesos"; }
}
