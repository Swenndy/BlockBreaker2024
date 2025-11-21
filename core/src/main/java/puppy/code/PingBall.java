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

    // NUEVO: estrategia de movimiento
    private BallMovementStrategy movementStrategy = new NormalMovementStrategy();

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

    // NUEVO: permitir cambiar la estrategia desde fuera (PowerUp o Game)
    public void setMovementStrategy(BallMovementStrategy strategy) {
        this.movementStrategy = strategy;
    }

    @Override
    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.circle(x, y, size);
    }

    // NUEVO: método interno que realmente mueve la bola
    void actualizarPosicion(float mult) {
        x += xSpeed * mult;
        y += ySpeed * mult;

        if (x - size < 0 || x + size > Gdx.graphics.getWidth()) {
            xSpeed = -xSpeed;
        }
        if (y + size > Gdx.graphics.getHeight()) {
            ySpeed = -ySpeed;
        }
    }

    // NUEVO: pequeño cambio aleatorio de dirección (usado por CrazyMovementStrategy)
    void ajustarDireccionAleatoria() {
        // ejemplo simple: variación pequeña en la velocidad horizontal
        float delta = (float)(Math.random() * 0.6f - 0.3f); // entre -0.3 y 0.3
        xSpeed += delta;
    }

    // update con Strategy
    public void update(float mult) {
        if (estaQuieto) return;
        movementStrategy.move(this, mult);
    }

    // colisión con bloques
    public void checkCollision(Block block) {
        if (!block.isDestroyed() && collidesWith(block)) {
            block.hit();
            ySpeed = -ySpeed;
        }
    }

    // colisión con paleta (igual que antes)
    public void checkCollision(Paddle paddle) {
        if (!collidesWith(paddle)) return;

        float paddleCenter = paddle.getX() + paddle.getWidth() / 2f;
        float offset = (x - paddleCenter) / (paddle.getWidth() / 2f);

        if (offset < -1f) offset = -1f;
        if (offset > 1f)  offset = 1f;

        float newXSpeed = offset * 6f;
        float newYSpeed = Math.abs(ySpeed);
        if (newYSpeed < 4f) newYSpeed = 4f;

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
