package com.mich.snake.strategies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public interface SkinStrategy {
    // Definimos que necesita hacer cualquier skin, sin decir COMO
    void dibujarCuerpo(SpriteBatch batch, Vector2 pos, float tam, boolean esCabeza);
    void dibujarComida(SpriteBatch batch, Vector2 pos, float tam);
    void dibujarFondo(SpriteBatch batch, int anchoPantalla, int altoPantalla, int tamCelda);
    Color getColorBordes();
    String getNombreComida();
    Color getColorObstaculos();
}