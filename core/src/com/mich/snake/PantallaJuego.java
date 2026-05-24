package com.mich.snake;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mich.snake.strategies.SkinStrategy;

public class PantallaJuego implements Screen {
    private static final int TAM_CELDA = 20;
    private static final int BORDE = 20;
    boolean esperandoSiguienteNivel = false;
    final SnakeGame game; // Referencia al "Director"
    Serpiente serpiente;
    Comida comida;
    float velocidadJuego = 0.15f;
    float timer = 0;
    String tipoSkin;
    Array<Obstaculo> obstaculos;
    int nivelActual = 1;
    int puntosActuales = 0;
    int metaComida = 3; // Cuantas debe comer para pasar de nivel
    int cantidadObstaculos = 3;
    BitmapFont font;
    private SkinStrategy skinActual;
    
    public PantallaJuego(final SnakeGame game, SkinStrategy estrategiaElegida) {
        this.game = game;
        reiniciarSerpiente();
        comida = new Comida(15, 15);
        font = new BitmapFont();
        font.getData().setScale(1.2f);
        obstaculos = new Array<>();
        
        // Asignamos la estrategia que viene desde el menú
        this.skinActual = estrategiaElegida;
        
        generarObstaculos();
    }
    @Override
    public void render(float delta) {
        // 1. LIMPIAR PANTALLA
        Gdx.gl.glClearColor(0.2f, 0.5f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

     // 2. CONTROLES (Flechas y WASD)
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

        // 3. LOGICA DE TIEMPO Y MOVIMIENTO
        if (!esperandoSiguienteNivel) {

            timer += delta;

            if(timer > velocidadJuego) {
                timer = 0;
            
                // 1. Calculamos si comió usando .getPosicion() de la clase abstracta
                boolean comio = (serpiente.getCabeza().x == comida.getPosicion().x && serpiente.getCabeza().y == comida.getPosicion().y);
                
                // 2. Si comió, delegamos de forma polimórfica (Suma puntos, spawnea y revisa nivel)
                if(comio) {
                    comida.alColisionar(this);
                }
            
            // PRIMERO: Movemos al animal
            	serpiente.mover(comio);
            
            // SEGUNDO: Revisamos colision con bordes 
            	int maxX = Gdx.graphics.getWidth() / TAM_CELDA;
            	int maxY = Gdx.graphics.getHeight() / TAM_CELDA;

            	float x = serpiente.getCabeza().x;
            	float y = serpiente.getCabeza().y;

            	if (x <= 0 || x >= maxX - 1 ||
            			y <= 0 || y >= maxY - 1) {

            		reiniciarJuego();
            	}
            	
            	for (Obstaculo obs : obstaculos) { // <-- Cambiado de Vector2 a Obstaculo
                    // Comparamos la posición de la cabeza RECIÉN MOVIDA con la piedra usando .getPosicion()
                    if (serpiente.getCabeza().x == obs.getPosicion().x && serpiente.getCabeza().y == obs.getPosicion().y) {
                        // ¡Polimorfismo en acción! El obstáculo sabe que debe reiniciar el juego
                        obs.alColisionar(this); 
                        break;
                    }
                }
            
            // CUARTO: Revisar si mordio los objetos recolectados (Solo si tiene)
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
        
        // PASO A: Dibujar el fondo correspondiente con SpriteBatch según el animal (GM-7)
        game.batch.begin();
        skinActual.dibujarFondo(game.batch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), TAM_CELDA);
        game.batch.end();

        // PASO B: Dibujar los elementos geométricos (Obstáculos y Bordes)
        game.shape.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        
        game.shape.setColor(skinActual.getColorObstaculos()); 
        for (Obstaculo obs : obstaculos) { 
            // Le pedimos la coordenada x e y a la posición que heredó del padre
            game.shape.rect(obs.getPosicion().x * TAM_CELDA, obs.getPosicion().y * TAM_CELDA, TAM_CELDA, TAM_CELDA);
        }

        // Bordes 
        game.shape.setColor(skinActual.getColorBordes());
        game.shape.rect(0, 0, Gdx.graphics.getWidth(), BORDE);
        game.shape.rect(0, Gdx.graphics.getHeight() - BORDE, Gdx.graphics.getWidth(), BORDE);
        game.shape.rect(0, 0, BORDE, Gdx.graphics.getHeight());
        game.shape.rect(Gdx.graphics.getWidth() - BORDE, 0, BORDE, Gdx.graphics.getHeight());

        game.shape.end();
        
        // 5. DIBUJO DE ELEMENTOS DEL JUEGO (Michi/Mascota y HUD)
        game.batch.begin();
        skinActual.dibujarComida(game.batch, comida.getPosicion(), TAM_CELDA);
        
        for (int i = 0; i < serpiente.getCuerpo().size; i++) {
            skinActual.dibujarCuerpo(game.batch, serpiente.getCuerpo().get(i), TAM_CELDA, (i == 0));
        }
        
        font.setColor(com.badlogic.gdx.graphics.Color.BLACK); 
        float alturaHUD = Gdx.graphics.getHeight() - 7; 
        
        font.draw(game.batch, "Nivel: " + nivelActual, TAM_CELDA, alturaHUD);
        font.draw(game.batch, skinActual.getNombreComida() + ": " + puntosActuales + "/" + metaComida, 510, alturaHUD);
        
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE); 
        game.batch.end();
        
        
     // 6. CAPA DE SIGUIENTE NIVEL (Fondo oscuro y texto central)
        if (esperandoSiguienteNivel) {
            // Dibujamos el rectangulo de pausa oscuro
            Gdx.gl.glEnable(GL20.GL_BLEND);
            game.shape.begin(ShapeRenderer.ShapeType.Filled);
            game.shape.setColor(0, 0, 0, 0.7f); // Un poco mas oscuro (0.7f)
            game.shape.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            game.shape.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            // Textos de victoria
            game.batch.begin();
            font.draw(game.batch, "Pasaste al nivel " + (nivelActual + 1) + "!", 
                      Gdx.graphics.getWidth() / 2f - 120, Gdx.graphics.getHeight() / 2f + 40);
            font.draw(game.batch, "[ ENTER ] para continuar", 
                      Gdx.graphics.getWidth() / 2f - 100, Gdx.graphics.getHeight() / 2f);
            game.batch.end();

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                iniciarSiguienteNivel();
            }
        }
    }
    
    public void spawnComida() {
        int maxCeldasX = Gdx.graphics.getWidth() / TAM_CELDA;
        int maxCeldasY = Gdx.graphics.getHeight() / TAM_CELDA;

        boolean posicionValida = false;

        while (!posicionValida) {
            posicionValida = true;

            int x = (int)(Math.random() * (maxCeldasX - 2)) + 1;
            int y = (int)(Math.random() * (maxCeldasY - 2)) + 1;

            // 1. Revisar cuerpo de la mascota
            for (Vector2 parte : serpiente.getCuerpo()) {
                if (parte.x == x && parte.y == y) {
                    posicionValida = false;
                    break;
                }
            }

            // 2. Revisar obstáculos (Cambiado de Vector2 a Obstaculo, usando .getPosicion())
            for (Obstaculo obs : obstaculos) {
                if (obs.getPosicion().x == x && obs.getPosicion().y == y) {
                    posicionValida = false;
                    break;
                }
            }

            // 3. Si la posición es libre, actualizamos la coordenada de la comida
            if (posicionValida) {
                comida.setPosicion(x, y);
            }
        }
    }
    
   public void reiniciarJuego() {
    	reiniciarSerpiente();
        puntosActuales = 0;
        nivelActual = 1;
        metaComida = 3;         // <-- IMPORTANTE! Volver al requisito del Nivel 1
        cantidadObstaculos = 3; // <-- Volver a las 3 piedras iniciales
        generarObstaculos();
        spawnComida();
    }
    
    private void generarObstaculos() {
        obstaculos.clear();

        int maxCeldasX = Gdx.graphics.getWidth() / TAM_CELDA;
        int maxCeldasY = Gdx.graphics.getHeight() / TAM_CELDA;

        for (int i = 0; i < cantidadObstaculos; i++) {

            int startX = (int)(Math.random() * (maxCeldasX - 5)) + 1;
            int startY = (int)(Math.random() * (maxCeldasY - 5)) + 1;

            boolean horizontal = Math.random() > 0.5;

            for (int j = 0; j < 3; j++) {

                float x = horizontal ? startX + j : startX;
                float y = horizontal ? startY : startY + j;

                boolean ocupado = false;

                // 1. Revisar comida (Usando .getPosicion())
                if (x == comida.getPosicion().x && y == comida.getPosicion().y) {
                    ocupado = true;
                }

                // 2. Revisar cuerpo completo de la mascota
                for (Vector2 parte : serpiente.getCuerpo()) {
                    if (parte.x == x && parte.y == y) {
                        ocupado = true;
                        break;
                    }
                }

                // 3. Revisar bordes
                if (x <= 0 || x >= maxCeldasX - 1 ||
                    y <= 0 || y >= maxCeldasY - 1) {
                    ocupado = true;
                }

                if (ocupado) {
                    continue;
                }

                // 4. Revisar si el obstáculo ya existe (Cambiado a Obstaculo y usando .getPosicion())
                boolean yaExiste = false;
                for (Obstaculo obs : obstaculos) {
                    if (obs.getPosicion().x == x && obs.getPosicion().y == y) {
                        yaExiste = true;
                        break;
                    }
                }

                // 5.Instanciamos nuestra clase hija
                if (!yaExiste) {
                    obstaculos.add(new Obstaculo((int)x, (int)y));
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
    
    // Estos metodos son obligatorios por la interfaz Screen, pero pueden estar vacios por ahora
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
    }
}
