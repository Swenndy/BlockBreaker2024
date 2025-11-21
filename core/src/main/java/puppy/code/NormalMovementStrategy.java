package puppy.code;

public class NormalMovementStrategy implements BallMovementStrategy {

    @Override
    public void move(PingBall ball, float baseMultiplier) {
        ball.actualizarPosicion(baseMultiplier);
    }
}
