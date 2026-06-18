package com.mich.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;

public class PantallaLeaderboard implements Screen {
    final SnakeGame game;
    BitmapFont font;
    Preferences prefs;

    // Listas para almacenar el Top 10 en memoria temporal
    private Array<String> nombres;
    private Array<Integer> puntajes;
    private Array<String> dificultades;

    // Variables para el registro de un nuevo record
    private boolean calificaParaTop10 = false;
    private int puntajeLogrado;
    private String dificultadLograda;
    private StringBuilder nombreIngresado;
    private boolean nombreGuardado = false;

    // Constructor cuando vienes a VER la leaderboard desde el menu principal
    public PantallaLeaderboard(final SnakeGame game) {
        this.game = game;
        this.font = new BitmapFont();
        this.font.getData().setScale(1.5f);
        this.prefs = Gdx.app.getPreferences("SnakeMascotasHighscores");
        this.nombreGuardado = true; // No hay nada que guardar, solo miramos
        cargarLeaderboard();
    }

    // Constructor cuando vienes directo de MORIR en una partida
    public PantallaLeaderboard(final SnakeGame game, int puntaje, String dificultad) {
        this.game = game;
        this.font = new BitmapFont();
        this.font.getData().setScale(1.5f);
        this.prefs = Gdx.app.getPreferences("SnakeMascotasHighscores");
        this.puntajeLogrado = puntaje;
        this.dificultadLograda = dificultad;
        this.nombreIngresado = new StringBuilder("");

        cargarLeaderboard();
        verificarSiEntraEnTop(puntaje);
    }

    private void cargarLeaderboard() {
        nombres = new Array<>();
        puntajes = new Array<>();
        dificultades = new Array<>();

        // Cargamos los 10 puestos guardados en el disco. Si no existen, ponemos valores por defecto.
        for (int i = 1; i <= 10; i++) {
            nombres.add(prefs.getString("nom_" + i, "VACIO"));
            puntajes.add(prefs.getInteger("pts_" + i, 0));
            dificultades.add(prefs.getString("dif_" + i, "---"));
        }
    }

    private void verificarSiEntraEnTop(int nuevoPuntaje) {
        // Si el puntaje es mayor que cero y (la lista no esta llena de records reales o supera al ultimo puesto)
        if (nuevoPuntaje > 0 && (puntajes.get(9) == 0 || nuevoPuntaje > puntajes.get(9))) {
            calificaParaTop10 = true;
            nombreGuardado = false;
            // Abrimos el teclado en pantalla en dispositivos moviles por si acaso
            Gdx.input.setOnscreenKeyboardVisible(true); 
        } else {
            nombreGuardado = true; // No califica, no hay nada que bloquear
        }
    }

    private void guardarNuevoRecord(String nombreFinal) {
        if (nombreFinal.isEmpty()) nombreFinal = "MASCOTA";

        // Encontrar la posicion correcta para insertar el nuevo puntaje ordenadamente
        int posicionInsercion = 10;
        for (int i = 0; i < 10; i++) {
            if (puntajeLogrado > puntajes.get(i)) {
                posicionInsercion = i;
                break;
            }
        }

        // Desplazar e insertar el nuevo elemento en las listas en memoria
        if (posicionInsercion < 10) {
            nombres.insert(posicionInsercion, nombreFinal);
            puntajes.insert(posicionInsercion, puntajeLogrado);
            dificultades.insert(posicionInsercion, dificultadLograda);

            // Recortar para mantener solo el Top 10
            nombres.truncate(10);
            puntajes.truncate(10);
            dificultades.truncate(10);

            // Escribir los cambios definitivos en las Preferences de LibGDX
            for (int i = 0; i < 10; i++) {
                prefs.putString("nom_" + (i + 1), nombres.get(i));
                prefs.putInteger("pts_" + (i + 1), puntajes.get(i));
                prefs.putString("dif_" + (i + 1), dificultades.get(i));
            }
            prefs.flush(); // Guarda fisicamente el archivo en el disco
        }
        nombreGuardado = true;
        Gdx.input.setOnscreenKeyboardVisible(false);
    }

    private void leerEntradaTeclado() {
        // Capturar letras para el nombre (maximo 5 caracteres para mantener la estetica ordenada)
        for (int key = Input.Keys.A; key <= Input.Keys.Z; key++) {
            if (Gdx.input.isKeyJustPressed(key) && nombreIngresado.length() < 5) {
                nombreIngresado.append(Input.Keys.toString(key));
            }
        }
        // Permitir borrar con Backspace
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && nombreIngresado.length() > 0) {
            nombreIngresado.setLength(nombreIngresado.length() - 1);
        }
        // Confirmar con Enter
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            guardarNuevoRecord(nombreIngresado.toString());
        }
    }

    @Override
    public void render(float delta) {
        // Fondo azul oscuro espacial para la Leaderboard
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Si el jugador rompio un record, leemos su teclado
        if (calificaParaTop10 && !nombreGuardado) {
            leerEntradaTeclado();
        }

        game.batch.begin();

        // 1. TITULO DE LA PANTALLA
        font.setColor(com.badlogic.gdx.graphics.Color.GOLD);
        font.getData().setScale(1.8f);
        font.draw(game.batch, "TABLA DE MEJORES PUNTAJES", 140, 450);

        // --- CASO ESPECIAL: PANEL DE ESCRITURA FLOTANTE SI CLASIFICO“ ---
        if (calificaParaTop10 && !nombreGuardado) {
            font.setColor(com.badlogic.gdx.graphics.Color.GREEN);
            font.getData().setScale(1.2f);
            font.draw(game.batch, "TU RECORD: " + nombreIngresado.toString() + (System.currentTimeMillis() % 1000 < 500 ? "_" : "") 
                    + "  |  DIF: " + dificultadLograda + "  |  PTS: " + puntajeLogrado, 130, 410);
        }

        // 2. ENCABEZADOS DE LA TABLA
        font.setColor(com.badlogic.gdx.graphics.Color.CYAN);
        font.getData().setScale(1.2f);
        font.draw(game.batch, "POS", 100, 370);
        font.draw(game.batch, "NOMBRE", 180, 370);
        font.draw(game.batch, "DIFICULTAD", 340, 370);
        font.draw(game.batch, "PUNTAJE", 500, 370);

        // 3. PINTAR EL TOP 10 REAL (SIN SALTARSE NADA)
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        for (int i = 0; i < 10; i++) {
            float yPos = 340 - (i * 24); // Separacion vertical ordenada

            // Si el nombre esta vacio o es el por defecto, lo pintamos mas tenue
            if (nombres.get(i).equals("VACIO")) {
                font.setColor(com.badlogic.gdx.graphics.Color.GRAY);
            } else {
                font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
            }

            // Registro real de la base de datos
            font.draw(game.batch, String.format("%02d", (i + 1)), 100, yPos);
            font.draw(game.batch, nombres.get(i), 180, yPos);
            font.draw(game.batch, dificultades.get(i), 340, yPos);
            font.draw(game.batch, String.valueOf(puntajes.get(i)), 500, yPos);
        }

        // 4. PANEL INFERIOR DE INSTRUCCIONES
        if (!nombreGuardado) {
            font.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
            font.getData().setScale(1.1f);
            font.draw(game.batch, "¡RECORD ALCANZADO! Escribe tu nombre y presiona [ ENTER ]", 90, 75);
        } else {
            font.setColor(com.badlogic.gdx.graphics.Color.ORANGE);
            font.getData().setScale(1.2f);
            font.draw(game.batch, "[ J ] Jugar de Nuevo           [ M ] Volver al Menu", 130, 75);

            // Captura de navegacion posterior
            if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                game.setScreen(new SelectorPantalla(game));
                dispose();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                game.setScreen(new PantallaMenu(game));
                dispose();
            }
        }

        game.batch.end();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { font.dispose(); }
}