package puppy.code;

import com.badlogic.gdx.graphics.Color;

public class PowerUpSlow extends PowerUp {

    public PowerUpSlow(float x, float y) {
        super(x, y);
        color = Color.CYAN;  // color del power-up
    }

    @Override
    protected void applyEffect(GameState gs, Paddle paddle, BlockBreakerGame game) {
        // reduce ligeramente la velocidad del juego
        game.setBallSpeedModifier(0.8f);
        game.showPowerUpMessage("¡Pelota más lenta!");
    }
}
