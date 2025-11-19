package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class PowerUp extends GameObject {

    // ya NO redefinimos x, y (vienen de GameObject)
    protected float speed = 150f;           // velocidad de caída
    protected Color color = Color.WHITE;

    public PowerUp(float x, float y) {
        // ancho y alto 20x20 para la caja de colisión
        super(x, y, 20, 20);
        color = Color.WHITE;
    }

    // TEMPLATE METHOD: este es el que debería llamar el juego
    public final void activate(GameState gs, Paddle paddle, BlockBreakerGame game) {
        playSound();
        applyEffect(gs, paddle, game);
    }

    protected void playSound() {
        // opcional: sonido
    }

    protected abstract void applyEffect(GameState gs, Paddle paddle, BlockBreakerGame game);

    public void update(float delta) {
        // usamos y heredada de GameObject
        y -= speed * delta;
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        // círculo centrado en el rectángulo
        float cx = x + width  / 2f;
        float cy = y + height / 2f;
        shape.circle(cx, cy, width / 2f);
    }

    // colisión AABB (rectángulo vs rectángulo) más robusta
    public boolean collidesWith(Paddle pad) {
        boolean colX = x < pad.getX() + pad.getWidth()  && x + width  > pad.getX();
        boolean colY = y < pad.getY() + pad.getHeight() && y + height > pad.getY();
        return colX && colY;
    }

    public boolean isOut() {
        return y + height < 0;   // salió completamente por abajo
    }

    public float getY() {
        return y;
    }
}
