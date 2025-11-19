package puppy.code;

import com.badlogic.gdx.graphics.Color;

public class PowerUpLife extends PowerUp {

    public PowerUpLife(float x, float y) {
        super(x, y);
        color = Color.PINK;  // color del power-up en pantalla
    }

    @Override
    protected void applyEffect(GameState gs, Paddle paddle, BlockBreakerGame game) {
        gs.setVidas(gs.getVidas() + 1);  // +1 vida
        game.showPowerUpMessage("¡Vida extra!");
    }
}
