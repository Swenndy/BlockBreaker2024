package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Paddle extends GameObject {

    public Paddle(float x, float y, float ancho, float alto) {
        super(x, y, ancho, alto);
    }

    @Override
    public void draw(ShapeRenderer shape){
        shape.setColor(Color.BLUE);

        float nuevoX = x;

        // Movimiento
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            nuevoX = x - 15;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            nuevoX = x + 15;

        // Ajuste de límites (si se sale, lo empujamos dentro)
        if (nuevoX < 0) {
            nuevoX = 0;
        }
        if (nuevoX + width > Gdx.graphics.getWidth()) {
            nuevoX = Gdx.graphics.getWidth() - width;
        }

        x = nuevoX;

        shape.rect(x, y, width, height);
    }
    
    public void setWidth(float w) {
        this.width = Math.min(w, 400); // máximo ancho
    }

    public float getWidth() {
        return width;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getHeight() {
        return height;
    }

}
