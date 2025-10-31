package puppy.code;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import java.util.Random;

public class Block extends GameObject implements Damageable {
    protected Color color;
    protected boolean destroyed;
    private final Random rng;
    protected int life;   // cantidad de golpes que puede recibir

    // Constructor normal (1 golpe)
    public Block(float x, float y, float width, float height) {
        this(x, y, width, height, 1);
    }

    // Constructor con cantidad de vidas personalizada
    public Block(float x, float y, float width, float height, int life) {
        super(x, y, width, height);
        this.life = life;
        this.destroyed = false;
        this.rng = new Random((int)(x + y));

        // Generar color aleatorio suave
        float r = 0.4f + rng.nextFloat() * 0.6f;
        float g = 0.4f + rng.nextFloat() * 0.6f;
        float b = 0.4f + rng.nextFloat() * 0.6f;
        this.color = new Color(r, g, b, 1f);
    }

    // Método al recibir un golpe
    @Override
    public void hit() {
        life--;
        if (life <= 0) {
            destroyed = true;
        } else {
            // Cambia el color para mostrar daño visual
            color = new Color(color.r * 0.8f, color.g * 0.8f, color.b * 0.8f, 1f);
        }
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void draw(ShapeRenderer shape) {
        if (!destroyed) {
            shape.setColor(color);
            shape.rect(x, y, width, height);
        }
    }

    public Color getColor() { return color; }

    // Getter opcional si querís consultar la vida
    public int getLife() { return life; }
}
