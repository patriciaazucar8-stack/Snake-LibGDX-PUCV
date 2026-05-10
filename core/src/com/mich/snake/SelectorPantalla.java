package com.mich.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        font.draw(game.batch, "ELIGE A TU MICHI", 220, 450);
        font.draw(game.batch, "1. Gato Negro", 100, 300);
        font.draw(game.batch, "2. Gato Tuxedo", 100, 200);
        font.draw(game.batch, "3. Gato Naranjo", 100, 100);
        game.batch.end();

        // Lógica de selección con teclado por ahora (luego será con clics en caritas)
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_1)) {
            // Aquí iría el miau del negro
            empezarJuego("NEGRO");
        }
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_2)) {
            // Aquí iría el miau del tuxedo
            empezarJuego("TUXEDO");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            empezarJuego("NARANJO");
        }
    }

    private void empezarJuego(String skin) {
        // Por ahora solo saltamos al juego, 
        // pronto le pasaremos la skin a la PantallaJuego
    	game.setScreen(new PantallaJuego(game, skin)); // <-- ¡Le pasamos la skin elegida!
        dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { font.dispose(); }
}
