package com.mich.snake;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Serpiente {
    private Array<Vector2> cuerpo;
    private int direccion;

    public Serpiente(int x, int y) {
        cuerpo = new Array<Vector2>();
        cuerpo.add(new Vector2(x, y));
        direccion = Input.Keys.RIGHT;
    }

    public void mover(boolean crecio) {
        Vector2 cabeza = cuerpo.first();
        Vector2 nuevaCabeza = new Vector2(cabeza.x, cabeza.y);

        if(direccion == Input.Keys.UP) nuevaCabeza.y++;
        else if(direccion == Input.Keys.DOWN) nuevaCabeza.y--;
        else if(direccion == Input.Keys.LEFT) nuevaCabeza.x--;
        else if(direccion == Input.Keys.RIGHT) nuevaCabeza.x++;

        cuerpo.insert(0, nuevaCabeza);
        if (!crecio) {
            cuerpo.removeIndex(cuerpo.size - 1);
        }
    }

    // Getters y Setters
    public Vector2 getCabeza() { return cuerpo.first(); }
    public Array<Vector2> getCuerpo() { return cuerpo; }
    public void setDireccion(int dir) { this.direccion = dir; }
}
