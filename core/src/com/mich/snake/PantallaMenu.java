package com.mich.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Input;

public class PantallaMenu implements Screen {
    final SnakeGame game;
    BitmapFont font; // Para escribir texto en pantalla

    public PantallaMenu(final SnakeGame game) {
        this.game = game;
        font = new BitmapFont(); // Usa la fuente por defecto de LibGDX
        font.getData().setScale(2); // La hacemos un poco más grande
    }
    
    @Override
    public void render(float delta) {
        // Fondo un poco más "púrpura" para variar el menú
        Gdx.gl.glClearColor(0.2f, 0.1f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        game.batch.begin();
        
        // FUENTE PARA LOS TÍTULOS 
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        font.getData().setScale(2.0f);
        // ----------------------------------------
        
        font.draw(game.batch, "DOMESTIC-SNAKE", 170, 400); // Centrado según el largo del texto
        font.draw(game.batch, "Haz clic para empezar...", 150, 200);
        
        // Instrucciones
        font.setColor(com.badlogic.gdx.graphics.Color.YELLOW); // Un gris suave 
        font.getData().setScale(0.9f); // Un tamaño sutil
        
        String controles = "Controles: Muevete con WASD o las flechas del teclado";
        String salirMenu = "[ESC] para salir del juego";
        
        font.draw(game.batch, controles, 30, 70);
        font.draw(game.batch, salirMenu, 30, 45);
        game.batch.end();

        // Si el usuario presiona ESCAPE, cerramos el juego inmediatamente
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        // Si el usuario hace clic o toca la pantalla...
        if (Gdx.input.isTouched()) {
            game.setScreen(new SelectorPantalla(game)); 
            dispose(); 
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    
    @Override 
    public void dispose() {
        font.dispose();
    }
}