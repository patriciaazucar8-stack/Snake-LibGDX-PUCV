package com.mich.snake;

import com.mich.snake.strategies.SkinStrategy;
import com.mich.snake.strategies.GatoStrategy;
import com.mich.snake.strategies.PerroStrategy;
import com.mich.snake.strategies.PezStrategy;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Input;

public class SelectorPantalla implements Screen {
    final SnakeGame game;
    BitmapFont font;

    public SelectorPantalla(final SnakeGame game) {
        this.game = game;
        font = new BitmapFont();
        font.getData().setScale(1.5f);
    }
    
    @Override
    public void render(float delta) {
        // Fondo oscuro para que resalten los personajes
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        
        // --- RESETEAR FUENTE PARA EL TÍTULO Y TEXTOS (Evita que se queden amarillos y chicos) ---
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        font.getData().setScale(1.5f);
        // ---------------------------------------------------------------------------------------
        
        // Título de la pantalla
        font.draw(game.batch, "ELIGE A TU MASCOTA", 200, 410);

        // Cada imagen la dibujamos de un tamaño de 64x64 pixeles para que se vean grandes y claras
        
        // 1. Gato (Izquierda) + su etiqueta
        game.batch.draw(RecursosJuego.getInstance().texMichi, 150, 220, 64, 64);
        font.draw(game.batch, "[ 1 ] Gato", 130, 190);

        // 2. Perro (Centro) + su etiqueta
        game.batch.draw(RecursosJuego.getInstance().texPerro, 290, 220, 64, 64);
        font.draw(game.batch, "[ 2 ] Perro", 280, 190);

        // 3. Pez (Derecha) + su etiqueta
        game.batch.draw(RecursosJuego.getInstance().texPez, 430, 220, 64, 64);
        font.draw(game.batch, "[ 3 ] Pez", 420, 190);

        // -------------------------------------------------------------
        // INDICACIÓN DE CONTROLES (Abajo en el centro)
        // -------------------------------------------------------------
        font.setColor(com.badlogic.gdx.graphics.Color.YELLOW); 
        font.getData().setScale(1.0f); 
        
        String instruccionSeleccion = "Selecciona tu mascota usando los numeros 1, 2 o 3";
        
        // Lo centramos en la parte inferior
        font.draw(game.batch, instruccionSeleccion, Gdx.graphics.getWidth() / 2f - 200, 50);
        game.batch.end();

        // La lógica de teclado 
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            empezarJuego(new GatoStrategy());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            empezarJuego(new PerroStrategy());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            empezarJuego(new PezStrategy());
        }
    }

    private void empezarJuego(SkinStrategy estrategiaElegida) {
        game.setScreen(new PantallaJuego(game, estrategiaElegida)); 
        dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { font.dispose(); }
}