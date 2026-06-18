package com.mich.snake.strategies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mich.snake.RecursosJuego; // Importe Singleton

public class GatoStrategy implements SkinStrategy {

    @Override
    public void dibujarCuerpo(SpriteBatch batch, Vector2 pos, float tam, boolean esCabeza) {
        if (esCabeza) {
            // Imagen (40px)
            float offset = (40 - tam) / 2f; 
            batch.draw(RecursosJuego.getInstance().texMichi, 
                       pos.x * tam - offset, 
                       pos.y * tam - offset, 40, 40);
        } else {
            // Lanas rosadas pequenias
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
        return new Color(0.9f, 0.75f, 0.3f, 1f); // Bordes grises para que no desentone
    }
    @Override
    public Color getColorObstaculos() {
        // Color naranja
    	return new Color(0.85f, 0.45f, 0.25f, 1f);
    }
    @Override
    public void dibujarFondo(SpriteBatch batch, int anchoPantalla, int altoPantalla, int tamCelda) {
        int celdasX = anchoPantalla / tamCelda;
        int celdasY = altoPantalla / tamCelda;
        
        //Color verde musgo
        batch.setColor(1.0f, 0.95f, 0.55f, 1f); 

        for (int x = 1; x < celdasX - 1; x += 4) {
            for (int y = 1; y < celdasY - 1; y += 4) {
                batch.draw(RecursosJuego.getInstance().texPasto, x * tamCelda, y * tamCelda, tamCelda * 4, tamCelda * 4);
            }
        }
        
        // Restauramos el color a blanco puro para no te�ir el HUD ni los personajes
        batch.setColor(com.badlogic.gdx.graphics.Color.WHITE);
    }
    @Override
    public String getNombreComida() { return "Lanas"; }
	
}