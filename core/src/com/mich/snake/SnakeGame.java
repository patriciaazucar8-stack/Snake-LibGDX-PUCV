package com.mich.snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SnakeGame extends Game {
    public ShapeRenderer shape;
    public SpriteBatch batch;

    @Override
    public void create() {
            shape = new ShapeRenderer();
            batch = new SpriteBatch();
            
            //Iniciamos en el menú
            this.setScreen(new PantallaMenu(this));
    }

    @Override
    public void render() {
        // Llamamos al render del Game para que dibuje la pantalla activa
        super.render();
    }

    @Override
    public void dispose() {
        shape.dispose();
        batch.dispose();
    }
}