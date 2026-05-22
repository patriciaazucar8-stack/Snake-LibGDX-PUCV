package com.mich.snake;
import com.mich.snake.strategies.SkinStrategy; // <-- AGREGA ESTE IMPORT
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
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        // 1. ACTUALIZAMOS LOS TEXTOS PARA LAS NUEVAS MASCOTAS DOMÉSTICAS
        font.draw(game.batch, "ELIGE A TU MASCOTA", 200, 450);
        font.draw(game.batch, "1. Gato", 100, 300);
        font.draw(game.batch, "2. Perro", 100, 200);
        font.draw(game.batch, "3. Pez", 100, 100);
        game.batch.end();

        // 2. CONECTAMOS LAS TECLAS CON SUS ESTRATEGIAS CORRESPONDIENTES
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_1)) {
            empezarJuego(new GatoStrategy());
        }
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_2)) {
            empezarJuego(new PerroStrategy());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            empezarJuego(new PezStrategy());
        }
    }

    // 3. CAMBIAMOS EL MÉTODO PARA QUE RECIBA LA INTERFAZ SKINSTRATEGY
    private void empezarJuego(SkinStrategy estrategiaElegida) {
        // Le pasamos el objeto concreto (gato, perro o pez) a la PantallaJuego
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
