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
    
    private String dificultadSeleccionada = "MEDIO"; 

    public SelectorPantalla(final SnakeGame game) {
        this.game = game;
        font = new BitmapFont();
        font.getData().setScale(1.5f);
    }
    
    @Override
    public void render(float delta) {
        // Fondo oscuro
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        
        // Titulo
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        font.getData().setScale(1.5f);
        font.draw(game.batch, "CONFIGURA TU PARTIDA", 190, 440);

        // --- SECCIÓN DE DIFICULTAD ---
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        font.getData().setScale(1.1f);
        font.draw(game.batch, "DIFICULTAD:", 80, 380);

        // Dibujamos "FACIL"
        if (dificultadSeleccionada.equals("FACIL")) font.setColor(com.badlogic.gdx.graphics.Color.GREEN);
        else font.setColor(com.badlogic.gdx.graphics.Color.GRAY);
        font.draw(game.batch, "[ FACIL ]", 230, 380);

        // Dibujamos "MEDIO"
        if (dificultadSeleccionada.equals("MEDIO")) font.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
        else font.setColor(com.badlogic.gdx.graphics.Color.GRAY);
        font.draw(game.batch, "[ MEDIO ]", 340, 380);

        // Dibujamos "DIFICIL"
        if (dificultadSeleccionada.equals("DIFICIL")) font.setColor(com.badlogic.gdx.graphics.Color.RED);
        else font.setColor(com.badlogic.gdx.graphics.Color.GRAY);
        font.draw(game.batch, "[ DIFICIL ]", 450, 380);


        // --- SECCIÓN DE SELECCIÓN DE MASCOTA ---
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        font.getData().setScale(1.3f);
        font.draw(game.batch, "ELIGE A TU MASCOTA", 200, 270);

        // 1. Gato
        game.batch.draw(RecursosJuego.getInstance().texMichi, 150, 140, 64, 64);
        font.draw(game.batch, "[ 1 ] Gato", 130, 110);

        // 2. Perro
        game.batch.draw(RecursosJuego.getInstance().texPerro, 290, 140, 64, 64);
        font.draw(game.batch, "[ 2 ] Perro", 280, 110);

        // 3. Pez
        game.batch.draw(RecursosJuego.getInstance().texPez, 430, 140, 64, 64);
        font.draw(game.batch, "[ 3 ] Pez", 420, 110);
        
        game.batch.end();

        
        // --- LOGICA DE DETECCIÓN DE CLICS DEL MOUSE ---
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); 

            if (mouseY >= 360 && mouseY <= 390) {
                // Rango X para [ FACIL ] 
                if (mouseX >= 220 && mouseX <= 310) {
                    dificultadSeleccionada = "FACIL";
                }
                // Rango X para [ MEDIO ]
                else if (mouseX >= 330 && mouseX <= 420) {
                    dificultadSeleccionada = "MEDIO";
                }
                // Rango X para [ DIFICIL ]
                else if (mouseX >= 440 && mouseX <= 530) {
                    dificultadSeleccionada = "DIFICIL";
                }
            }
        }

        // La logica de teclado original para las mascotas
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

    // Aqui conectamos el builder
    private void empezarJuego(SkinStrategy estrategiaElegida) {
        Partida config = new PartidaBuilder()
                .conDificultad(dificultadSeleccionada)
                .conMascota(estrategiaElegida)
                .build();

        game.setScreen(new PantallaJuego(game, config)); 
        dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { font.dispose(); }
}