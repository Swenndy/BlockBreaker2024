package puppy.code;

import java.util.ArrayList;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import puppy.code.gfx.BrickTextures;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.Texture.TextureFilter;


public class BlockBreakerGame extends ApplicationAdapter {
        private OrthographicCamera camera;
	private SpriteBatch batch;	   
	private BitmapFont font;
	private ShapeRenderer shape;
	private ArrayList<PingBall> balls = new ArrayList<>();
        private final int[] extraBallAt = {10, 25, 45};
        private int nextThresholdIdx = 0;
	private Paddle pad;
	private ArrayList<Block> blocks = new ArrayList<>();
        private Viewport uiViewport; 
        private BrickTextures brickTextures;
        private int texW = 64, texH = 20, radius = 5;
        private float nivelAlpha = 0f; // transparencia
        private long nivelStartTime = 0; // momento en que cambió el nivel
        private final float NIVEL_MOSTRAR_DURACION = 2.5f; // segundos
        private GameState gs;
        private ArrayList<PowerUp> powerUps = new ArrayList<>();
        private float ballSpeedModifier = 1f;
        private String powerUpMsg = "";
        private float powerUpMsgAlpha = 0f;
        private long powerUpMsgStart = 0;
        private final float POWERUP_MOSTRAR_DURACION = 1.8f; // segundos

        public void setBallSpeedModifier(float modifier) {
            this.ballSpeedModifier = modifier;
        }

        public float getBallSpeedModifier() {
            return ballSpeedModifier;
        }
        public void showPowerUpMessage(String msg) {
            powerUpMsg = msg;
            powerUpMsgAlpha = 1f;
            powerUpMsgStart = System.currentTimeMillis();
        }

    
		@Override
		public void create () {	
                camera = new OrthographicCamera();
                camera.setToOrtho(false, 1280, 720);
                batch = new SpriteBatch();
                brickTextures = new BrickTextures();
                gs = GameState.get();

                // fuentes
                FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/fuenteLinda.ttf"));
                FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
                p.size = 32;
                p.minFilter = TextureFilter.Linear;
                p.magFilter = TextureFilter.Linear;
                font = gen.generateFont(p);
                gen.dispose();
                gs.reset();
                
                crearBloques(2 + gs.getNivel());
                uiViewport = new ScreenViewport();
                shape = new ShapeRenderer();
                pad = new Paddle(Gdx.graphics.getWidth()/2 - 50, 40, 100, 10); // <-- crea la paleta primero

                // Inicializa lista de pelotas ANTES de spawnear
                balls = new ArrayList<>();

                 

                // ahora sí puedes crear la primera pelota
                spawnBall(true);   
		}
		public void crearBloques(int filas) {
                    blocks.clear();
                    int blockWidth = 70;
                    int blockHeight = 26;
                    int y = Gdx.graphics.getHeight();

                    for (int fila = 0; fila < filas; fila++) {
                        y -= blockHeight + 10;

                        for (int x = 5; x < Gdx.graphics.getWidth(); x += blockWidth + 10) {
                            Block bloque;

                            // 🔹 Cada 3ª fila o cada 5° bloque será más duro (2 golpes)
                            if ((fila + 1) % 3 == 0 || (x / (blockWidth + 10)) % 5 == 0) {
                                bloque = new BlockHard(x, y, blockWidth, blockHeight);
                            } else {
                                bloque = new Block(x, y, blockWidth, blockHeight);
                            }

                            blocks.add(bloque);
                        }
                    }   
		}
                protected void spawnBall(boolean attachToPaddle) {
                    int bx = Math.round(pad.getX() + pad.getWidth() / 2f - 5f);
                    int by = Math.round(pad.getY() + pad.getHeight() + 11f);
                    PingBall nb = new PingBall(bx, by, 10, 5, 7, attachToPaddle);
                    balls.add(nb);
                }
                private void resetGame() {
                gs.actualizarMejor();
                gs.reset();
                nextThresholdIdx = 0;

                crearBloques(2 + gs.getNivel());
                balls.clear();
                spawnBall(true);}
                
		public void dibujaTextos() {
                    uiViewport.apply();  // aplica el viewport del HUD
                    batch.setProjectionMatrix(uiViewport.getCamera().combined);

                    batch.begin();
                    font.setColor(0, 0, 0, 0.7f);  // sombra
                    font.draw(batch, "Puntos: " + gs.getPuntaje(), 14, 38);
                    font.draw(batch, "Vidas : " + gs.getVidas(), uiViewport.getWorldWidth() - 206, 38);

                    font.setColor(1, 1, 1, 1);     // texto principal
                    font.draw(batch, "Puntos: " + gs.getPuntaje(), 10, 40);
                    font.draw(batch, "Vidas : " + gs.getVidas(), uiViewport.getWorldWidth() - 210, 40);
                    font.setColor(1, 1, 0.6f, 1);
                    font.draw(batch, "Mejor: " + gs.getMejorPuntaje(), uiViewport.getWorldWidth()/2f - 70, 40);

                    // -----------------------------
                    //   TEXTO DEL NIVEL
                    // -----------------------------
                    if (nivelAlpha > 0f) {
                        long elapsed = System.currentTimeMillis() - nivelStartTime;
                        if (elapsed > NIVEL_MOSTRAR_DURACION * 1000) {
                            nivelAlpha -= Gdx.graphics.getDeltaTime() * 0.5f;
                            if (nivelAlpha < 0f) nivelAlpha = 0f;
                        }

                        font.setColor(1f, 1f, 1f, nivelAlpha * 0.4f);
                        String textoNivel = "Nivel " + gs.getNivel();

                        float x = uiViewport.getWorldWidth() / 2f - 100;
                        float y = uiViewport.getWorldHeight() / 2f + 20;

                        font.draw(batch, textoNivel, x, y);
                    }

                    // -----------------------------
                    //   TEXTO DE POWER-UP (AQUÍ!)
                    // -----------------------------
                    if (powerUpMsgAlpha > 0f) {

                        long elapsed = System.currentTimeMillis() - powerUpMsgStart;

                        if (elapsed > POWERUP_MOSTRAR_DURACION * 1000) {
                            powerUpMsgAlpha -= Gdx.graphics.getDeltaTime() * 0.7f;
                            if (powerUpMsgAlpha < 0f) powerUpMsgAlpha = 0f;
                        }

                        font.setColor(1f, 1f, 0f, powerUpMsgAlpha);
                        float x = uiViewport.getWorldWidth() / 2f - 150;
                        float y = uiViewport.getWorldHeight() - 90;

                        font.draw(batch, powerUpMsg, x, y);
                    }

                    // FIN
                    batch.end();
                }	
		
		@Override
                public void render () {
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                // LÓGICA
                boolean space = Gdx.input.isKeyPressed(Input.Keys.SPACE);
                ArrayList<PingBall> fallen = new ArrayList<>();

                for (PingBall b : balls) {
                    if (b.estaQuieto()) {
                        b.setXY(Math.round(pad.getX() + pad.getWidth()/2f - 5), Math.round(pad.getY() + pad.getHeight() + 11f));
                        if (space) b.setEstaQuieto(false);
                    } else {
                        float mult = 1f + gs.getNivel() * 0.03f;   // más suave que 0.05
                        if (mult > 1.4f) mult = 1.4f;      // tope
                        b.update(mult * ballSpeedModifier);
                    }
                    if (b.getY() < 0) fallen.add(b);
                }


                // remover caídas
                for (PingBall f : fallen) balls.remove(f);

                // si todas cayeron: perder vida y reponer 1 pegada (o reset si no quedan vidas)
                if (!fallen.isEmpty() && balls.isEmpty()) {
                    gs.perderVida();
                    if (gs.getVidas() > 0) {
                        spawnBall(true);
                    } else {
                        resetGame();
                        return; // salimos del render este frame
                    }
                }

                if (gs.getVidas() <= 0) {
                    resetGame();
                }

                if (blocks.size() == 0) {
                    gs.subirNivel();
                    crearBloques(2 + gs.getNivel());
                    balls.clear();
                    spawnBall(true);
                    
                    nivelAlpha = 1f;
                    nivelStartTime = System.currentTimeMillis();
                }

                for (Block blk : blocks){
                    for (PingBall b : balls){
                        b.checkCollision(blk);
                    }
                }

                for (int i = 0; i < blocks.size(); i++) {
                    if (blocks.get(i).isDestroyed()) {

                        gs.sumarPuntos(1);

                        // --- DROP DE POWER-UP (40% de probabilidad) ---
                        if (Math.random() < 0.4) {

                            int px = (int) blocks.get(i).getX();
                            int py = (int) blocks.get(i).getY();

                            double r = Math.random();

                            if (r < 0.25)
                                powerUps.add(new PowerUpExpand(px, py));
                            else if (r < 0.50)
                                powerUps.add(new PowerUpSlow(px, py));
                            else if (r < 0.75)
                                powerUps.add(new PowerUpMultiBall(px, py));
                            else
                                powerUps.add(new PowerUpLife(px, py));
                        }

                        // --- eliminar bloque ---
                        blocks.remove(i);
                        i--;

                        // --- extra ball por puntaje ---
                        if (nextThresholdIdx < extraBallAt.length
                                && gs.getPuntaje() >= extraBallAt[nextThresholdIdx]) {

                            spawnBall(true);
                            nextThresholdIdx++;
                        }
                    }
                }

                for (PingBall b : balls){
                    b.checkCollision(pad);
                }
                ArrayList<PowerUp> collected = new ArrayList<>();

                for (PowerUp p : powerUps) {
                    p.update(Gdx.graphics.getDeltaTime()); // baja hacia abajo

                    // si toca la paleta → activar
                    if (p.collidesWith(pad)) {
                        p.applyEffect(gs, pad, this);
                        collected.add(p);
                    }

                    // si se cae del mapa → borrar
                    if (p.isOut()) {
                        collected.add(p);
                    }
                }

                powerUps.removeAll(collected);

                
                camera.update();
                batch.setProjectionMatrix(camera.combined);
                batch.enableBlending();                     
                batch.begin();
                for (Block b : blocks) {
                    
                    Color base = b.getColor() != null ? new Color(b.getColor()) : new Color(0.55f, 0.75f, 1f, 1f);

                    Texture tex = brickTextures.get(texW, texH, radius, base);

                    // sombra
                    batch.setColor(0, 0, 0, 0.25f);
                    batch.draw(tex, b.getX() + 2, b.getY() - 2, b.getWidth(), b.getHeight());

                    // ladrillo
                    batch.setColor(1, 1, 1, 1);
                    batch.draw(tex, b.getX(), b.getY(), b.getWidth(), b.getHeight());
                }
                batch.end();

                // DIBUJO DE PALETA Y BOLA (ShapeRenderer)
                shape.begin(ShapeRenderer.ShapeType.Filled);
                pad.draw(shape);
                for (PingBall b : balls) b.draw(shape);
                for (PowerUp p : powerUps) {
                    p.draw(shape);
                }
                shape.end();

                // HUD
                dibujaTextos();
            }
        @Override
        public void resize(int width, int height) {
            uiViewport.update(width, height, true);
        }
}
