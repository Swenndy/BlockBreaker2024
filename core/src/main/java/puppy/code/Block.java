package puppy.code;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import java.util.Random;

public class Block extends GameObject {
    private Color color;
    private boolean destroyed;
    private final Random rng;

    public Block(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.destroyed = false;
        this.rng = new Random((int)(x + y));

        // color random pero no feo
        float r = 0.4f + rng.nextFloat() * 0.6f;
        float g = 0.4f + rng.nextFloat() * 0.6f;
        float b = 0.4f + rng.nextFloat() * 0.6f;
        this.color = new Color(r, g, b, 1f);
    }

    @Override
    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.rect(x, y, width, height);
    }

    public Color getColor() { return color; }

    public boolean isDestroyed() { return destroyed; }
    public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }
}
