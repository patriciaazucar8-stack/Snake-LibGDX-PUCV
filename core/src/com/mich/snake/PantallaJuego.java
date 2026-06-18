package com.mich.snake;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mich.snake.strategies.VelocidadStrategy;
import com.mich.snake.strategies.VelocidadBase;
import com.mich.snake.strategies.VelocidadLenta;
import com.mich.snake.strategies.VelocidadRapida;
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
    boolean juegoTerminado = false;         // Controla si el jugador murió
    float puntajeFinalCalculado = 0;       // El puntaje total sumando niveles y % de objetos
    float puntajeAnimado = 0;              // El contador dinámico que va a ir subiendo en pantalla
    boolean registroCompletado = false;    // Para saber cuándo pasar a la Leaderboard
    final SnakeGame game; 
    Serpiente serpiente;
    Comida comida;
    float velocidadJuego;
    float timer = 0;
    private VelocidadStrategy estrategiaVelocidad = new VelocidadBase();
    private float velocidadOriginalBase;
    String tipoSkin;
    Array<Obstaculo> obstaculos;
    int nivelActual = 1;
    int puntosActuales = 0;
    int metaComida = 3; // Cuantas debe comer para pasar de nivel
    int cantidadObstaculos = 3; // cantidad de obstaculos inicial
    BitmapFont font;
    private SkinStrategy skinActual;
    
    private String nombreDificultad;
    
    public PantallaJuego(final SnakeGame game, Partida config) {
        this.game = game;
        reiniciarSerpiente();
        comida = new Comida(15, 15);
        font = new BitmapFont();
        font.getData().setScale(1.2f);
        obstaculos = new Array<>();
        
        // Extraemos los datos del builder
        this.skinActual = config.getSkinMascota();
        // Convertimos los milisegundos del Builder a floats
        this.velocidadJuego = config.getVelocidad() / 1000f; 
        this.nombreDificultad = config.getDificultadNombre();
        
        // === MUEVE ESTA LÍNEA AQUÍ (Debajo de velocidadJuego) ===
        this.velocidadOriginalBase = this.velocidadJuego;
        
        if (nombreDificultad.equals("DIFICIL")) {
            this.metaComida = 4;
        } else {
            this.metaComida = 3;
        }
        
        generarObstaculos();
    }
    
    
    @Override
    public void render(float delta) {
        // 1. LIMPIAR PANTALLA
        Gdx.gl.glClearColor(0.2f, 0.5f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

     // 2. CONTROLES (Flechas y WASD)
        if (!juegoTerminado) {
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
        }
     // --- 3. LOGICA DE TIEMPO Y MOVIMIENTO ---
        if (!esperandoSiguienteNivel && !juegoTerminado) {

            timer += delta;
            
            float velocidadActualizada = estrategiaVelocidad.modificarVelocidad(velocidadOriginalBase);

            if(timer > velocidadActualizada) {
                timer = 0;
            
                // Calculamos si comió usando .getPosicion() de la clase abstracta
                boolean comio = (serpiente.getCabeza().x == comida.getPosicion().x && serpiente.getCabeza().y == comida.getPosicion().y);
                
                // Si comió, delegamos de forma polimórfica (Suma puntos, spawnea y revisa nivel)
                if(comio) {
                    // 1. Se dispara el Template Method (Suma el punto base en Comida.java)
                    comida.procesarColision(this);
                    
                    // 2. ¡AQUÍ VA EL SPAWN! Le exigimos a PantallaJuego reubicar la comida aleatoriamente
                    spawnComida(); 

                    // 3. Puntaje por dificultad
                    if (nombreDificultad.equals("DIFICIL")) {
                        puntosActuales += 1;
                    }
                    if (puntosActuales >= metaComida) {
                        esperandoSiguienteNivel = true;
                    }
                }
            
                // Movemos al animal (crece si 'comio' fue true)
                serpiente.mover(comio);
            
                // Revisamos colision con bordes 
                int maxX = Gdx.graphics.getWidth() / TAM_CELDA;
                int maxY = Gdx.graphics.getHeight() / TAM_CELDA;

                float x = serpiente.getCabeza().x;
                float y = serpiente.getCabeza().y;

                if (x <= 0 || x >= maxX - 1 || y <= 0 || y >= maxY - 1) {
                    activarGameOver();
                }
                
                // Revisamos colisión con los obstáculos (Piedras)
                for (Obstaculo obs : obstaculos) { 
                    if (serpiente.getCabeza().x == obs.getPosicion().x && serpiente.getCabeza().y == obs.getPosicion().y) {
                        obs.procesarColision(this);
                        break;
                    }
                }
            
                // Revisar si se mordió su propio cuerpo
                for (int i = 1; i < serpiente.getCuerpo().size; i++) {
                    Vector2 parte = serpiente.getCuerpo().get(i);
                    if (serpiente.getCabeza().x == parte.x && serpiente.getCabeza().y == parte.y) {
                        activarGameOver();
                        break;
                    }
                }
            }
        }

     // 4. DIBUJAR 
        
        // Dibujar el fondo correspondiente con SpriteBatch segun el animal 
        game.batch.begin();
        skinActual.dibujarFondo(game.batch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), TAM_CELDA);
        game.batch.end();

        // Dibujar los elementos geometricos (Obstaculos y Bordes)
        game.shape.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        
        game.shape.setColor(skinActual.getColorObstaculos()); 
        for (Obstaculo obs : obstaculos) { 
            // Le pedimos la coordenada x e y a la posición que hereda del padre
            game.shape.rect(obs.getPosicion().x * TAM_CELDA, obs.getPosicion().y * TAM_CELDA, TAM_CELDA, TAM_CELDA);
        }

        // Bordes 
        game.shape.setColor(skinActual.getColorBordes());
        game.shape.rect(0, 0, Gdx.graphics.getWidth(), BORDE);
        game.shape.rect(0, Gdx.graphics.getHeight() - BORDE, Gdx.graphics.getWidth(), BORDE);
        game.shape.rect(0, 0, BORDE, Gdx.graphics.getHeight());
        game.shape.rect(Gdx.graphics.getWidth() - BORDE, 0, BORDE, Gdx.graphics.getHeight());

        game.shape.end();
        
     // 5. DIBUJO DE ELEMENTOS DEL JUEGO (Mascota, comida y HUD)
        game.batch.begin();
        skinActual.dibujarComida(game.batch, comida.getPosicion(), TAM_CELDA);
        
        for (int i = 0; i < serpiente.getCuerpo().size; i++) {
            skinActual.dibujarCuerpo(game.batch, serpiente.getCuerpo().get(i), TAM_CELDA, (i == 0));
        }
        
        font.setColor(com.badlogic.gdx.graphics.Color.BLACK); 
        float alturaHUD = Gdx.graphics.getHeight() - 6; 
        
        font.draw(game.batch, "Nivel: " + nivelActual, TAM_CELDA, alturaHUD);
        font.draw(game.batch, skinActual.getNombreComida() + ": " + puntosActuales + "/" + metaComida, 510, alturaHUD);
        
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE); 
        game.batch.end();
        
        
     // 6. CAPA DE SIGUIENTE NIVEL (Fondo oscuro y texto central)
        if (esperandoSiguienteNivel) {
            // Dibujamos el rectangulo de pausa oscuro
            Gdx.gl.glEnable(GL20.GL_BLEND);
            game.shape.begin(ShapeRenderer.ShapeType.Filled);
            game.shape.setColor(0, 0, 0, 0.7f); 
            game.shape.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            game.shape.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            
         // MENSAJES PERSONALIZADOS SEG�N LA DIFICULTAD
            String mensajeVictoria = "¡Bien hecho! Sigue así"; 
            if (nombreDificultad.equals("DIFICIL")) {
                mensajeVictoria = "¡Sobreviviste al nivel! Eres una maquina";
            } else if (nombreDificultad.equals("MEDIO")) {
                mensajeVictoria = "¡Buen ritmo! No te rindas";
            }
            // Textos de victoria
            game.batch.begin();
            font.draw(game.batch, mensajeVictoria, 
                      Gdx.graphics.getWidth() / 2f - 180, Gdx.graphics.getHeight() / 2f + 40); 
            font.draw(game.batch, "[ ENTER ] para continuar", 
                      Gdx.graphics.getWidth() / 2f - 100, Gdx.graphics.getHeight() / 2f);
            game.batch.end();

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                iniciarSiguienteNivel();
            }
        }
        if (juegoTerminado) {
        	Gdx.gl.glEnable(GL20.GL_BLEND);
        	Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        	
        	game.shape.begin(ShapeRenderer.ShapeType.Filled);
        	game.shape.setColor(0f, 0f, 0f, 0.75f);
        	game.shape.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        	game.shape.end();
        	Gdx.gl.glDisable(GL20.GL_BLEND);
        	
        	// Subida de puntaje animado por frame
        	if(puntajeAnimado < puntajeFinalCalculado) {
        		puntajeAnimado += 2;
        		if(puntajeAnimado > puntajeFinalCalculado) {
        			puntajeAnimado = puntajeFinalCalculado;
        		}
        	}
        	
        	game.batch.begin();
        	font.setColor(com.badlogic.gdx.graphics.Color.RED);
        	font.getData().setScale(2f);
        	font.draw(game.batch, "¡MASCOTA DEBILITADA!", Gdx.graphics.getWidth() / 2f - 180, Gdx.graphics.getHeight() / 2f + 80);
        	
        	font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        	font.getData().setScale(1.3f);
        	// Mostrar la dificultad
        	font.draw(game.batch, "Dificultad: " + nombreDificultad, Gdx.graphics.getWidth() / 2f - 90, Gdx.graphics.getHeight() / 2f + 20);
        	// Mostrar puntaje animado (Casteado)
        	font.draw(game.batch, "Puntaje Obtenido: " + (int)puntajeAnimado, Gdx.graphics.getWidth() / 2f - 110, Gdx.graphics.getHeight() / 2f - 20);
        	
        	if(puntajeAnimado == puntajeFinalCalculado) {
        		font.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
        		font.draw(game.batch, "Presiona [ ENTER ] para continuar", Gdx.graphics.getWidth() / 2f - 170, Gdx.graphics.getHeight() / 2f - 80);
        		
        		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
        			game.setScreen(new PantallaLeaderboard(game, (int)puntajeFinalCalculado, nombreDificultad));
        			dispose();
        		}
        	}
        	game.batch.end();
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

            // Revisar cuerpo de la mascota
            for (Vector2 parte : serpiente.getCuerpo()) {
                if (parte.x == x && parte.y == y) {
                    posicionValida = false;
                    break;
                }
            }

            // Revisar obstaculos
            for (Obstaculo obs : obstaculos) {
                if (obs.getPosicion().x == x && obs.getPosicion().y == y) {
                    posicionValida = false;
                    break;
                }
            }

            // Si la posición es libre, actualizamos la coordenada de la comida
            if (posicionValida) {
                comida.setPosicion(x, y);
            }
        }
    }
    
    public void reiniciarJuego() {
        juegoTerminado = false;
        puntajeFinalCalculado = 0;
        puntajeAnimado = 0;
            
        // === DEJA ESTA LÍNEA LIMPIA ASÍ ===
        this.estrategiaVelocidad = new VelocidadBase();
            
        reiniciarSerpiente();
        puntosActuales = 0;
        nivelActual = 1;
        metaComida = 3;          
        cantidadObstaculos = 3; 
        generarObstaculos();
        spawnComida();
            
        esperandoSiguienteNivel = false;
    }
   
   public void activarGameOver() {
	   if (!juegoTerminado) {
		   juegoTerminado = true;
		   
		   int puntosPorNivel = (nivelActual - 1) * 100;
		   
		   int puntosPorObjetos = puntosActuales * 25;
		   
		   puntajeFinalCalculado = puntosPorNivel + puntosPorObjetos;
		   puntajeAnimado = 0;
	   }
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

                // Revisar comida (Usando .getPosicion())
                if (x == comida.getPosicion().x && y == comida.getPosicion().y) {
                    ocupado = true;
                }

                // Revisar cuerpo completo de la mascota
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

                // Revisar si el obstaculo ya existe 
                boolean yaExiste = false;
                for (Obstaculo obs : obstaculos) {
                    if (obs.getPosicion().x == x && obs.getPosicion().y == y) {
                        yaExiste = true;
                        break;
                    }
                }

                // Instanciamos nuestra clase hija
                if (!yaExiste) {
                    obstaculos.add(new Obstaculo((int)x, (int)y));
                }
            }
        }
    }
    
    private void iniciarSiguienteNivel() {

        nivelActual++;
        puntosActuales = 0;
        
        // === PARADOJA DE VELOCIDAD DINÁMICA (STRATEGY) ===
        if (esNivelPrimoPermitido(nivelActual)) {
            if (nombreDificultad.equals("DIFICIL")) {
                this.estrategiaVelocidad = new VelocidadLenta(); // De Difícil pasa a Fácil (Respiro)
            } else if (nombreDificultad.equals("MEDIO")) {
                this.estrategiaVelocidad = new VelocidadRapida(); // De Medio pasa a Difícil (Presión)
            } else if (nombreDificultad.equals("FACIL")) {
                // De Fácil pasa a velocidad de modo MEDIO (Desafío)
                this.estrategiaVelocidad = new VelocidadStrategy() {
                    @Override
                    public float modificarVelocidad(float velocidadBase) { return 0.12f; }
                };
            }
        } else {
            // Si el nivel NO es primo, vuelve a la velocidad elegida por el jugador en el menú
            this.estrategiaVelocidad = new VelocidadBase(); 
        }
        // =================================================
        
        if (nombreDificultad.equals("DIFICIL")) {
            metaComida += 2; // Sube de 4 -> 6 -> 8 -> 10... Siempre par
        } else {
            metaComida += 2; // Sube de 3 -> 5 -> 7 -> 9... Siempre impar (ya que sumas de a 1)
        }
        
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
    
    private boolean esNivelPrimoPermitido(int nivel) {
        if (nivel == 2) return false; // Excluimos el nivel 2 por tu regla
        if (nivel <= 1) return false; // El nivel 1 tampoco es primo
        
        for (int i = 2; i <= Math.sqrt(nivel); i++) {
            if (nivel % i == 0) return false;
        }
        return true; // Retorna true si es 3, 5, 7, 11...
    }
    
    // Estos metodos son obligatorios por la interfaz Screen
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
