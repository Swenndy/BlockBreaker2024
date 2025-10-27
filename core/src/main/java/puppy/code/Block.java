package puppy.code;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import java.util.Random;

public class Block {
    int x, y, width, height;
    Color cc;
    boolean destroyed;
    private final Random rng;

    public Block(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.destroyed = false;

        this.rng = new Random(x + y);

        // Colores válidos (0..1) y alfa 1.0
        this.cc = new Color(rng.nextFloat(), rng.nextFloat(), rng.nextFloat(), 1f);
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(cc);
        shape.rect(x, y, width, height);
    }
}

