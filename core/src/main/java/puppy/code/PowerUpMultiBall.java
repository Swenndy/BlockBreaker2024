package puppy.code;

import com.badlogic.gdx.graphics.Color;

public class PowerUpMultiBall extends PowerUp {

    public PowerUpMultiBall(float x, float y) {
        super(x, y);
        color = Color.YELLOW;   // color del power-up
    }

    @Override
    protected void applyEffect(GameState gs, Paddle paddle, BlockBreakerGame game) {
        // crea 2 pelotas nuevas pegadas a la paleta
        game.spawnBall(true);
        game.spawnBall(true);
        game.showPowerUpMessage("¡Pelota extra!");
    }
}