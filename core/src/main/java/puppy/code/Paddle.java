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

        // trabajamos con los campos heredados: x, y, width, height
        float nuevoX = x;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  nuevoX = x - 15;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) nuevoX = x + 15;

        // límites pantalla
        if (nuevoX > 0 && nuevoX + width < Gdx.graphics.getWidth()) {
            x = nuevoX;
        }

        shape.rect(x, y, width, height);
    }
}
