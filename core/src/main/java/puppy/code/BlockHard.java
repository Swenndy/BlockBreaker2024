package puppy.code;

import com.badlogic.gdx.graphics.Color;

public class BlockHard extends Block {

    public BlockHard(float x, float y, float w, float h) {
        super(x, y, w, h, 2); // 2 vidas
        this.color = new Color(0.3f, 0.3f, 0.3f, 1f); // gris oscuro inicial
    }

    @Override
    public void hit() {
        life--;
        if (life == 1) {
            // cambia el color a uno aleatorio normal después del primer golpe
            float r = 0.4f + (float)Math.random() * 0.6f;
            float g = 0.4f + (float)Math.random() * 0.6f;
            float b = 0.4f + (float)Math.random() * 0.6f;
            this.color = new Color(r, g, b, 1f);
        } else if (life <= 0) {
            destroyed = true;
        }
    }
}
