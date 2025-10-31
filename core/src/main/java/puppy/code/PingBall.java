package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PingBall extends GameObject {
    private float size;
    private float xSpeed;
    private float ySpeed;
    private Color color = Color.WHITE;
    private boolean estaQuieto;

    public PingBall(float x, float y, float size, float xSpeed, float ySpeed, boolean iniciaQuieto) {
        super(x, y, size * 2, size * 2);
        this.size = size;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.estaQuieto = iniciaQuieto;
    }

    public boolean estaQuieto() { return estaQuieto; }
    public void setEstaQuieto(boolean v) { estaQuieto = v; }
    public void setXY(float nx, float ny) { this.x = nx; this.y = ny; }
    public float getY() { return y; }

    @Override
    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.circle(x, y, size);
    }

    // update con multiplicador
    public void update(float mult) {
        if (estaQuieto) return;

        x += xSpeed * mult;
        y += ySpeed * mult;

        if (x - size < 0 || x + size > Gdx.graphics.getWidth()) {
            xSpeed = -xSpeed;
        }
        if (y + size > Gdx.graphics.getHeight()) {
            ySpeed = -ySpeed;
        }
    }

    // colisión con bloques
    public void checkCollision(Block block) {
        if (!block.isDestroyed() && collidesWith(block)) {
            block.hit();
            ySpeed = -ySpeed;
        }
    }

    // colisión con paleta (mejorada)
    public void checkCollision(Paddle paddle) {
        if (!collidesWith(paddle)) return;

        // punto medio de la paleta
        float paddleCenter = paddle.getX() + paddle.getWidth() / 2f;
        // cuánto me desvié del centro (entre -1 y 1)
        float offset = (x - paddleCenter) / (paddle.getWidth() / 2f);

        // límite por si acaso
        if (offset < -1f) offset = -1f;
        if (offset > 1f)  offset = 1f;

        // velocidad horizontal en función del lugar de impacto
        float newXSpeed = offset * 6f;  // mientras más grande, más se va de lado

        // mantenemos una velocidad vertical mínima hacia arriba
        float newYSpeed = Math.abs(ySpeed);
        if (newYSpeed < 4f) newYSpeed = 4f; // que no quede plana

        this.xSpeed = newXSpeed;
        this.ySpeed = newYSpeed;
        this.color = Color.GREEN;
    }

    private boolean collidesWith(GameObject obj) {
        boolean intersectaX = (obj.getX() + obj.getWidth() >= x - size) &&
                              (obj.getX() <= x + size);
        boolean intersectaY = (obj.getY() + obj.getHeight() >= y - size) &&
                              (obj.getY() <= y + size);
        return intersectaX && intersectaY;
    }
}
