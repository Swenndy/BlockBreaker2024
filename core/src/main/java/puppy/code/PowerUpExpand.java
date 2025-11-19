package puppy.code;
import com.badlogic.gdx.graphics.Color;
public class PowerUpExpand extends PowerUp {

    public PowerUpExpand(float x, float y) {
        super(x, y);
        color = Color.GREEN;
    }

    @Override
    protected void applyEffect(GameState gs, Paddle paddle, BlockBreakerGame game) {
        paddle.setWidth(paddle.getWidth() + 40);
        game.showPowerUpMessage("¡Base expandida!");
    }
}