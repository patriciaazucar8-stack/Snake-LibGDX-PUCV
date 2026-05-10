package com.mich.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PantallaJuego implements Screen {
    final SnakeGame game; // Referencia al "Director"
    Serpiente serpiente;
    Vector2 comida;
    float timer = 0;
    String tipoSkin;
    Array<Vector2> obstaculos;
    int nivelActual = 1;
    int puntosActuales = 0;
    int metaComida = 3; // Cuántas debe comer para pasar de nivel
    int cantidadObstaculos = 3;
    BitmapFont font;

    public PantallaJuego(final SnakeGame game, String skin) {
        this.game = game;
        serpiente = new Serpiente(10, 10);
        this.tipoSkin = skin;
        comida = new Vector2(15, 15);
        font = new BitmapFont();
        font.getData().setScale(1.2f);
        obstaculos = new Array<>();
        generarObstaculos();
    }
    @Override
    public void render(float delta) {
        // 1. LIMPIAR PANTALLA
        Gdx.gl.glClearColor(0.2f, 0.5f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 2. CONTROLES (Leer el teclado)
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) serpiente.setDireccion(Input.Keys.UP);
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) serpiente.setDireccion(Input.Keys.DOWN);
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) serpiente.setDireccion(Input.Keys.LEFT);
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) serpiente.setDireccion(Input.Keys.RIGHT);

        // 3. LÓGICA DE TIEMPO Y MOVIMIENTO
        timer += delta;
        if(timer > 0.15f) {
            timer = 0;
            
            boolean comio = (serpiente.getCabeza().x == comida.x && serpiente.getCabeza().y == comida.y);
            if(comio) {
                puntosActuales++;
                spawnComida();
                
                if(puntosActuales >= metaComida) {
                    nivelActual++;
                    puntosActuales = 0;
                    metaComida = metaComida + 2; // Suma lineal: 5, 7, 9...
                    cantidadObstaculos = cantidadObstaculos + 2; // Suma lineal: 5, 8, 11...
                    
                    generarObstaculos();
                    System.out.println("Nivel: " + nivelActual + " | Piedras: " + cantidadObstaculos);
                }
            }
            
            // PRIMERO: Movemos al michi
            serpiente.mover(comio);
            
            // SEGUNDO: Revisamos colisión con bordes (Ya lo tienes)
            // Colisión con bordes de 10px
            // (Dividimos por 20 para saber en qué "celda" está la cabeza)
            if (serpiente.getCabeza().x < 0.5f || serpiente.getCabeza().x > (Gdx.graphics.getWidth() / 20) - 1 ||
            	serpiente.getCabeza().y < 0.5f || serpiente.getCabeza().y > (Gdx.graphics.getHeight() / 20) - 1) {
            	reiniciarJuego();
            }

            // TERCERO: Revisamos colisión con piedras (Incluso si solo es la cabeza)
            for (Vector2 obs : obstaculos) {
                // Comparamos la posición de la cabeza RECIÉN MOVIDA con la piedra
                if (serpiente.getCabeza().x == obs.x && serpiente.getCabeza().y == obs.y) {
                    reiniciarJuego();
                    break;
                }
            }
            
            // CUARTO: Revisar si se mordió la cola (Solo si tiene cuerpo)
            for (int i = 1; i < serpiente.getCuerpo().size; i++) {
                Vector2 parte = serpiente.getCuerpo().get(i);
                if (serpiente.getCabeza().x == parte.x && serpiente.getCabeza().y == parte.y) {
                    reiniciarJuego();
                    break;
                }
            }
        }


        // 4. DIBUJAR (Lo que ya tenías bien)
        game.shape.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        
        // Comida
        game.shape.setColor(1, 0, 0, 1); 
        game.shape.rect(comida.x * 20, comida.y * 20, 18, 18);

        // Skin del Michi
        if (tipoSkin.equals("NEGRO")) {
            game.shape.setColor(0.15f, 0.15f, 0.15f, 1);
        } else if (tipoSkin.equals("TUXEDO")) {
            game.shape.setColor(0.9f, 0.9f, 0.9f, 1);
        } else if (tipoSkin.equals("NARANJO")) {
            game.shape.setColor(1, 0.6f, 0, 1);
        } else {
            game.shape.setColor(0, 1, 1, 1);
        }

        for(Vector2 v : serpiente.getCuerpo()) {
            game.shape.rect(v.x * 20, v.y * 20, 18, 18);
        }
        
        game.shape.setColor(0.4f, 0.4f, 0.4f, 1); // Un gris un poco más claro
        for (Vector2 obs : obstaculos) {
            // Dibujamos de 20x20 para que se toquen entre sí y parezcan una pared continua
            game.shape.rect(obs.x * 20, obs.y * 20, 20, 20); 
        }
     // Gris intermedio para que resalte
        game.shape.setColor(0.5f, 0.5f, 0.5f, 1);

        // Abajo y Arriba
        game.shape.rect(0, 0, Gdx.graphics.getWidth(), 10); 
        game.shape.rect(0, Gdx.graphics.getHeight() - 10, Gdx.graphics.getWidth(), 10); 
        // Izquierda y Derecha
        game.shape.rect(0, 0, 10, Gdx.graphics.getHeight()); 
        game.shape.rect(Gdx.graphics.getWidth() - 10, 0, 10, Gdx.graphics.getHeight());
        game.shape.end();
        game.batch.begin();
     // 20 píxeles de margen desde el borde gris
        font.draw(game.batch, "Nivel: " + nivelActual, 20, Gdx.graphics.getHeight() - 25);
        font.draw(game.batch, "Michi-puntos: " + puntosActuales + "/" + metaComida, 20, Gdx.graphics.getHeight() - 45);
        game.batch.end();
    }
    private void spawnComida() {
    	comida.x = (int)(Math.random() * (Gdx.graphics.getWidth() / 20 - 2)) + 1;
    	comida.y = (int)(Math.random() * (Gdx.graphics.getHeight() / 20 - 2)) + 1;
    }
    
    private void reiniciarJuego() {
        serpiente = new Serpiente(10, 10);
        puntosActuales = 0;
        nivelActual = 1;
        metaComida = 3;         // <-- ¡IMPORTANTE! Volver al requisito del Nivel 1
        cantidadObstaculos = 3; // <-- Volver a las 5 piedras iniciales
        generarObstaculos();
    }
    
    private void generarObstaculos() {
        obstaculos.clear();
        
        for(int i = 0; i < cantidadObstaculos; i++) {
            // 1. Elegimos un punto inicial pero alejándonos de los bordes (-2 y +1)
            // El ancho de tu mundo en celdas es Gdx.graphics.getWidth() / 20
            int limiteX = (Gdx.graphics.getWidth() / 20) - 4;
            int limiteY = (Gdx.graphics.getHeight() / 20) - 4;

            int startX = (int)(Math.random() * limiteX) + 2;
            int startY = (int)(Math.random() * limiteY) + 2;
            
            boolean horizontal = Math.random() > 0.5;
            
            for(int j = 0; j < 3; j++) {
                float x = horizontal ? startX + j : startX;
                float y = horizontal ? startY : startY + j;
                
                // 2. REVISIÓN DE SEGURIDAD (La "zona prohibida")
                // No ponemos pared si está la comida O si está la cabeza del michi
                if ((x == comida.x && y == comida.y) || 
                    (x == serpiente.getCabeza().x && y == serpiente.getCabeza().y)) {
                    continue; 
                }
                
                obstaculos.add(new Vector2(x, y));
            }
        }
    }
    // Estos métodos son obligatorios por la interfaz Screen, pero pueden estar vacíos por ahora
    @Override
    public void show() {
        // Esto le dice al juego: "Oye, ahora pesca el teclado en esta pantalla"
        Gdx.input.setInputProcessor(null); 
    }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}