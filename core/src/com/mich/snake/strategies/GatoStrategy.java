package com.mich.snake.strategies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mich.snake.RecursosJuego; // Importe Singleton

public class GatoStrategy implements SkinStrategy {

    @Override
    public void dibujarCuerpo(SpriteBatch batch, Vector2 pos, float tam, boolean esCabeza) {
        if (esCabeza) {
            // Efecto Godzilla centrado (40px)
            float offset = (40 - tam) / 2f; 
            batch.draw(RecursosJuego.getInstance().texMichi, 
                       pos.x * tam - offset, 
                       pos.y * tam - offset, 40, 40);
        } else {
            // Lanas rosadas peque�as
            batch.draw(RecursosJuego.getInstance().texLana, 
                       pos.x * tam + 2, 
                       pos.y * tam + 2, 16, 16);
        }
    }

    @Override
    public void dibujarComida(SpriteBatch batch, Vector2 pos, float tam) {
        batch.draw(RecursosJuego.getInstance().texLana, pos.x * tam, pos.y * tam, tam, tam);
    }

    @Override
    public Color getColorBordes() {
        return new Color(0.76f, 0.745f, 0.745f, 1); // Bordes grises para que no desentone
    }
    @Override
    public Color getColorObstaculos() {
        // Un caf� tierra/madera (R: 0.4, G: 0.2, B: 0.1)
        return new Color(0.4f, 0.2f, 0.1f, 1);
    }
}