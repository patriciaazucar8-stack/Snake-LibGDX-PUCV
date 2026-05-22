package com.mich.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

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
        font.draw(game.batch, "DOMESTIC-SNAKE", 200, 400); // Centrado según el largo del texto
        font.draw(game.batch, "Haz clic para empezar...", 150, 200);
        game.batch.end();

        // Si el usuario hace clic o toca la pantalla...
        if (Gdx.input.isTouched()) {
            // ¡AHORA vamos al Selector en lugar de ir directo al juego!
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