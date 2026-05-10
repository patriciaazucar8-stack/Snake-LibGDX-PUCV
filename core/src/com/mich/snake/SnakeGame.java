package com.mich.snake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class SnakeGame extends ApplicationAdapter {
    ShapeRenderer shape;
    Serpiente serpiente; 
    Vector2 comida;
    float timer = 0;

    @Override
    public void create() {
        shape = new ShapeRenderer();
        serpiente = new Serpiente(10, 10); 
        comida = new Vector2(15, 15);
    }

    @Override
    public void render() {
        // 1. Limpiar pantalla (Fondo oscuro)
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 2. Controles (Actualizamos la dirección en la serpiente)
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) serpiente.setDireccion(Input.Keys.UP);
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) serpiente.setDireccion(Input.Keys.DOWN);
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) serpiente.setDireccion(Input.Keys.LEFT);
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) serpiente.setDireccion(Input.Keys.RIGHT);

        // 3. Lógica de tiempo y movimiento
        timer += Gdx.graphics.getDeltaTime();
        if(timer > 0.15f) {
            timer = 0;
            
            // Verificamos si la cabeza está sobre la comida
            boolean comio = (serpiente.getCabeza().x == comida.x && serpiente.getCabeza().y == comida.y);
            
            if(comio) {
                spawnComida();
            }
            
            // Movemos la serpiente
            serpiente.mover(comio);
            if (serpiente.getCabeza().x < 0 || serpiente.getCabeza().x >= Gdx.graphics.getWidth() / 20 ||
                    serpiente.getCabeza().y < 0 || serpiente.getCabeza().y >= Gdx.graphics.getHeight() / 20) {
                    
                    // Reiniciamos el objeto para que aparezca al medio otra vez
                    serpiente = new Serpiente(10, 10);
            }
        }

        // 4. Dibujar todo
        shape.begin(ShapeRenderer.ShapeType.Filled);
        
        // Dibujamos la comida (Rojo)
        shape.setColor(1, 0, 0, 1);
        shape.rect(comida.x * 20, comida.y * 20, 18, 18);
        
        // Dibujamos la serpiente (Cian)
        shape.setColor(0, 1, 1, 1);
        for(Vector2 v : serpiente.getCuerpo()) {
            shape.rect(v.x * 20, v.y * 20, 18, 18);
        }
        
        shape.end();
    }

    // Método para mover la comida a un lugar aleatorio
    private void spawnComida() {
        comida.x = (int)(Math.random() * (Gdx.graphics.getWidth() / 20));
        comida.y = (int)(Math.random() * (Gdx.graphics.getHeight() / 20));
    }

    @Override
    public void dispose() {
        shape.dispose();
    }
}