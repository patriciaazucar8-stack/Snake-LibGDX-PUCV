package com.mich.snake;

import com.badlogic.gdx.graphics.Texture;

public class RecursosJuego {
    private static RecursosJuego instance;
    public Texture texMichi, texLana, texPasto, texPerro, texHueso, texPez, texConcha;

    private RecursosJuego() {
        // Cargamos lo que ya tenemos firme
        texMichi = new Texture("cat.png");
        texLana = new Texture("lana.png");
        texPasto = new Texture("tile_0000.png");
        texPerro = new Texture("dog.png");
        texHueso = new Texture("hueso.png");
        texPez = new Texture("fish.png");
        texConcha = new Texture("concha.png");
        
    }

    public static RecursosJuego getInstance() {
        if (instance == null) instance = new RecursosJuego();
        return instance;
    }

    public void dispose() {
        if (texMichi != null) texMichi.dispose();
        if (texLana != null) texLana.dispose();
        if (texPasto != null) texPasto.dispose();
        if (texPerro != null) texPerro.dispose();
        if (texHueso != null) texHueso.dispose();
        if (texPez != null) texPez.dispose();
        if (texConcha != null) texConcha.dispose();
    }
}