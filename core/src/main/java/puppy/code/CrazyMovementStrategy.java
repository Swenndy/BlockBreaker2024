package puppy.code;

public class CrazyMovementStrategy implements BallMovementStrategy {

    @Override
    public void move(PingBall ball, float baseMultiplier) {
        // se mueve un poco más rápido
        ball.actualizarPosicion(baseMultiplier * 1.4f);

        // opcional: pequeño cambio extra en x para que sea menos predecible
        ball.ajustarDireccionAleatoria();
    }
}
