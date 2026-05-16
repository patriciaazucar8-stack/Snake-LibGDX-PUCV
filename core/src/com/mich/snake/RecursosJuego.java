package com.mich.snake;

import com.badlogic.gdx.graphics.Texture;

public class RecursosJuego {
    private static RecursosJuego instance;
    public Texture texMichi, texLana;

    private RecursosJuego() {
        // Cargamos lo que ya tenemos firme
        texMichi = new Texture("cat.png");
        texLana = new Texture("lana.png");
    }

    public static RecursosJuego getInstance() {
        if (instance == null) instance = new RecursosJuego();
        return instance;
    }

    public void dispose() {
        if (texMichi != null) texMichi.dispose();
        if (texLana != null) texLana.dispose();
    }
}