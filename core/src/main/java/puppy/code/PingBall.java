package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PingBall extends GameObject {

    private float xSpeed;
    private float ySpeed;
    private Color color = Color.WHITE;
    private boolean estaQuieto;
    private float radius;

    public PingBall(float x, float y, float size, float xSpeed, float ySpeed, boolean iniciaQuieto) {
        // en el padre: x, y, width, height
        super(x, y, size, size);
        this.radius = size;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.estaQuieto = iniciaQuieto;
    }

    public boolean estaQuieto() { return estaQuieto; }
    public void setEstaQuieto(boolean v) { this.estaQuieto = v; }

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getY() { return y; }

    @Override
    public void draw(ShapeRenderer shape){
        shape.setColor(color);
        shape.circle(x, y, radius);
    }

    public void update() {
        if (estaQuieto) return;

        x += xSpeed;
        y += ySpeed;

        // rebote horizontal
        if (x - radius < 0 || x + radius > Gdx.graphics.getWidth()) {
            xSpeed = -xSpeed;
        }
        // rebote techo
        if (y + radius > Gdx.graphics.getHeight()) {
            ySpeed = -ySpeed;
        }
    }

    public void checkCollision(Paddle paddle) {
        if (collidesWith(paddle)){
            color = Color.GREEN;
            ySpeed = -ySpeed;
        } else {
            color = Color.WHITE;
        }
    }

    private boolean collidesWith(Paddle pp) {
        boolean intersectaX = (pp.getX() + pp.getWidth() >= x - radius) &&
                              (pp.getX() <= x + radius);
        boolean intersectaY = (pp.getY() + pp.getHeight() >= y - radius) &&
                              (pp.getY() <= y + radius);
        return intersectaX && intersectaY;
    }

    public void checkCollision(Block block) {
        if (collidesWith(block)){
            ySpeed = -ySpeed;
            block.setDestroyed(true);
        }
    }

    private boolean collidesWith(Block bb) {
        boolean intersectaX = (bb.getX() + bb.getWidth() >= x - radius) &&
                              (bb.getX() <= x + radius);
        boolean intersectaY = (bb.getY() + bb.getHeight() >= y - radius) &&
                              (bb.getY() <= y + radius);
        return intersectaX && intersectaY;
    }
}
