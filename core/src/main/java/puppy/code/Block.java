package puppy.code;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import java.util.Random;

public class Block {
    private int x, y, width, height;
    private Color color;
    private boolean destroyed;
    private final Random rng;

    public Block(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.destroyed = false;
        this.rng = new Random(x + y);

        // Generar color aleatorio (más equilibrado)
        float r = 0.4f + rng.nextFloat() * 0.6f;
        float g = 0.4f + rng.nextFloat() * 0.6f;
        float b = 0.4f + rng.nextFloat() * 0.6f;
        this.color = new Color(r, g, b, 1f);
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.rect(x, y, width, height);
    }

    // Getters
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public Color getColor() { return color; }

    // Estado del bloque
    public boolean isDestroyed() { return destroyed; }
    public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }
}