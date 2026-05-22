package com.mich.snake;

import com.badlogic.gdx.graphics.Texture;

public class RecursosJuego {
    private static RecursosJuego instance;
    public Texture texMichi, texLana, texPasto;

    private RecursosJuego() {
        // Cargamos lo que ya tenemos firme
        texMichi = new Texture("cat.png");
        texLana = new Texture("lana.png");
        texPasto = new Texture("tile_0000.png");
    }

    public static RecursosJuego getInstance() {
        if (instance == null) instance = new RecursosJuego();
        return instance;
    }

    public void dispose() {
        if (texMichi != null) texMichi.dispose();
        if (texLana != null) texLana.dispose();
        if (texPasto != null) texPasto.dispose();
    }
}