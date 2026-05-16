package com.mich.snake;
import com.badlogic.gdx.graphics.Texture; 
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PantallaJuego implements Screen {
    private static final int TAM_CELDA = 20;
    private static final int TAM_GATO_VISUAL = 40;
    private static final int BORDE = 20;
    boolean esperandoSiguienteNivel = false;
    final SnakeGame game; // Referencia al "Director"
    Serpiente serpiente;
    Vector2 comida;
    float velocidadJuego = 0.15f;
    float timer = 0;
    String tipoSkin;
    Array<Vector2> obstaculos;
    int nivelActual = 1;
    int puntosActuales = 0;
    int metaComida = 3; // Cuántas debe comer para pasar de nivel
    int cantidadObstaculos = 3;
    BitmapFont font;
    Texture texMichi;
    Texture texLana;

    public PantallaJuego(final SnakeGame game, String skin) {
        this.game = game;
        reiniciarSerpiente();
        this.tipoSkin = skin;
        comida = new Vector2(15, 15);
        font = new BitmapFont();
        texMichi = new Texture("cat.png");
        texLana = new Texture("lana.png");
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
     // 2. CONTROLES (Ahora con Flechas y WASD)
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            serpiente.setDireccion(Input.Keys.UP);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            serpiente.setDireccion(Input.Keys.DOWN);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            serpiente.setDireccion(Input.Keys.LEFT);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            serpiente.setDireccion(Input.Keys.RIGHT);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit(); // Esto cierra la ventana del juego inmediatamente
        }

        // 3. LÓGICA DE TIEMPO Y MOVIMIENTO
        if (!esperandoSiguienteNivel) {

            timer += delta;

            if(timer > velocidadJuego) {
            	timer = 0;
            
            	boolean comio = (serpiente.getCabeza().x == comida.x && serpiente.getCabeza().y == comida.y);
            	if(comio) {
            		puntosActuales++;
            		spawnComida();
                
            		if(puntosActuales >= metaComida) {

            			esperandoSiguienteNivel = true;
            		}
            	}
            
            // PRIMERO: Movemos al michi
            	serpiente.mover(comio);
            
            // SEGUNDO: Revisamos colisión con bordes 
            	int maxX = Gdx.graphics.getWidth() / TAM_CELDA;
            	int maxY = Gdx.graphics.getHeight() / TAM_CELDA;

            	float x = serpiente.getCabeza().x;
            	float y = serpiente.getCabeza().y;

            	if (x <= 0 || x >= maxX - 1 ||
            			y <= 0 || y >= maxY - 1) {

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
        } 

        // 4. DIBUJAR 
        game.shape.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        
        game.shape.setColor(0.4f, 0.4f, 0.4f, 1); // Un gris un poco más claro
        for (Vector2 obs : obstaculos) {
            // Dibujamos de 20x20 para que se toquen entre sí y parezcan una pared continua
        	game.shape.rect(
        		    obs.x * TAM_CELDA,
        		    obs.y * TAM_CELDA,
        		    TAM_CELDA,
        		    TAM_CELDA
        		);
        }
     // Gris intermedio para que resalte
        game.shape.setColor(0.5f, 0.5f, 0.5f, 1);

        game.shape.rect(0, 0, Gdx.graphics.getWidth(), BORDE);

        game.shape.rect(
            0,
            Gdx.graphics.getHeight() - BORDE,
            Gdx.graphics.getWidth(),
            BORDE
        );

        game.shape.rect(0, 0, BORDE, Gdx.graphics.getHeight());

        game.shape.rect(
            Gdx.graphics.getWidth() - BORDE,
            0,
            BORDE,
            Gdx.graphics.getHeight()
        );
        game.shape.end(); // Cerramos el dibujo de geometría básica

        // 5. CAPA DE SIGUIENTE NIVEL (Fondo oscuro y texto central)
        if (esperandoSiguienteNivel) {
            // Dibujamos el rectángulo de pausa oscuro
            Gdx.gl.glEnable(GL20.GL_BLEND);
            game.shape.begin(ShapeRenderer.ShapeType.Filled);
            game.shape.setColor(0, 0, 0, 0.7f); // Un poco más oscuro (0.7f)
            game.shape.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            game.shape.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            // Dibujamos los textos de victoria
            game.batch.begin();
            font.draw(game.batch, "¡Pasaste al nivel " + (nivelActual + 1) + "!", 
                      Gdx.graphics.getWidth() / 2f - 120, Gdx.graphics.getHeight() / 2f + 40);
            font.draw(game.batch, "[ ENTER ] para continuar", 
                      Gdx.graphics.getWidth() / 2f - 100, Gdx.graphics.getHeight() / 2f);
            game.batch.end();

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                iniciarSiguienteNivel();
            }
        }

        // 6. DIBUJO DE ELEMENTOS DEL JUEGO (Michi y HUD)
     // 6. DIBUJO DE ELEMENTOS DEL JUEGO (Michi y HUD)
        game.batch.begin();

        // Primero dibujamos la COMIDA (El ovillo de lana que está suelto)
        game.batch.draw(texLana, comida.x * TAM_CELDA, comida.y * TAM_CELDA, TAM_CELDA, TAM_CELDA);

        // Ahora dibujamos la SERPIENTE segmento por segmento
        for (int i = 0; i < serpiente.getCuerpo().size; i++) {
            Vector2 v = serpiente.getCuerpo().get(i);
            
            if (i == 0) {
                // LA CABEZA: El gato grande (Michi-Godzilla)
                float offset = (TAM_GATO_VISUAL - TAM_CELDA) / 2f;
                game.batch.draw(
                    texMichi, 
                    v.x * TAM_CELDA - offset, 
                    v.y * TAM_CELDA - offset, 
                    TAM_GATO_VISUAL, 
                    TAM_GATO_VISUAL
                );
            } else {
                // EL CUERPO: Los ovillos de lana recolectados
                // Los dibujamos un pelín más chicos (16px) para que se vean como una hilera
                game.batch.draw(
                    texLana, 
                    v.x * TAM_CELDA + 2, 
                    v.y * TAM_CELDA + 2, 
                    16, 
                    16
                );
            }
        }

        // HUD: Nivel y Puntos
        font.draw(game.batch, "Nivel: " + nivelActual, TAM_CELDA, Gdx.graphics.getHeight() - 25);
        font.draw(game.batch, "Lanas: " + puntosActuales + "/" + metaComida, 20, Gdx.graphics.getHeight() - 45);

        game.batch.end();
    }
    private void spawnComida() {

        int maxCeldasX = Gdx.graphics.getWidth() / TAM_CELDA;
        int maxCeldasY = Gdx.graphics.getHeight() / TAM_CELDA;

        boolean posicionValida = false;

        while (!posicionValida) {

            posicionValida = true;

            int x = (int)(Math.random() * (maxCeldasX - 2)) + 1;
            int y = (int)(Math.random() * (maxCeldasY - 2)) + 1;

            // Revisar cuerpo
            for (Vector2 parte : serpiente.getCuerpo()) {
                if (parte.x == x && parte.y == y) {
                    posicionValida = false;
                    break;
                }
            }

            // Revisar obstáculos
            for (Vector2 obs : obstaculos) {
                if (obs.x == x && obs.y == y) {
                    posicionValida = false;
                    break;
                }
            }

            if (posicionValida) {
                comida.set(x, y);
            }
        }
    }
    
    private void reiniciarJuego() {
    	reiniciarSerpiente();
        puntosActuales = 0;
        nivelActual = 1;
        metaComida = 3;         // <-- ¡IMPORTANTE! Volver al requisito del Nivel 1
        cantidadObstaculos = 3; // <-- Volver a las 3 piedras iniciales
        generarObstaculos();
        spawnComida();
    }
    
    private void generarObstaculos() {

        obstaculos.clear();

        int maxCeldasX = Gdx.graphics.getWidth() / TAM_CELDA;
        int maxCeldasY = Gdx.graphics.getHeight() / TAM_CELDA;

        for(int i = 0; i < cantidadObstaculos; i++) {

            int startX = (int)(Math.random() * (maxCeldasX - 5)) + 1;
            int startY = (int)(Math.random() * (maxCeldasY - 5)) + 1;

            boolean horizontal = Math.random() > 0.5;

            for(int j = 0; j < 3; j++) {

                float x = horizontal ? startX + j : startX;
                float y = horizontal ? startY : startY + j;

                boolean ocupado = false;

                // Revisar comida
                if (x == comida.x && y == comida.y) {
                    ocupado = true;
                }

                // Revisar cuerpo completo
                for (Vector2 parte : serpiente.getCuerpo()) {
                    if (parte.x == x && parte.y == y) {
                        ocupado = true;
                        break;
                    }
                }

                // Revisar bordes
                if (x <= 0 || x >= maxCeldasX - 1 ||
                    y <= 0 || y >= maxCeldasY - 1) {

                    ocupado = true;
                }

                if (ocupado) {
                    continue;
                }

                // Revisar obstáculos duplicados
                boolean yaExiste = false;

                for (Vector2 obs : obstaculos) {
                    if (obs.x == x && obs.y == y) {
                        yaExiste = true;
                        break;
                    }
                }

                if (!yaExiste) {
                    obstaculos.add(new Vector2(x, y));
                }
            }
        }
    }
    private void iniciarSiguienteNivel() {

        nivelActual++;
        puntosActuales = 0;
        metaComida += 2;
        cantidadObstaculos += 2;
        reiniciarSerpiente();
        generarObstaculos();
        spawnComida();

        esperandoSiguienteNivel = false;
    }
    private void reiniciarSerpiente() {

        int centroX = (Gdx.graphics.getWidth() / TAM_CELDA) / 2;
        int centroY = (Gdx.graphics.getHeight() / TAM_CELDA) / 2;

        serpiente = new Serpiente(centroX, centroY);
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
    @Override
    public void dispose() {
        if (font != null) font.dispose();
        if (texMichi != null) texMichi.dispose();
        if (texLana != null) texLana.dispose();
    }
}