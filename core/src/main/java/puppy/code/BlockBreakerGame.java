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
        private int mejorPuntaje;
	private Paddle pad;
	private ArrayList<Block> blocks = new ArrayList<>();
	private int vidas;
	private int puntaje;
	private int nivel;
        private Viewport uiViewport; 
        private BrickTextures brickTextures;
        private int texW = 64, texH = 20, radius = 5;
    
		@Override
		public void create () {	
                camera = new OrthographicCamera();
                camera.setToOrtho(false, 1280, 720);
                batch = new SpriteBatch();
                brickTextures = new BrickTextures();

                // fuentes
                FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/fuenteLinda.ttf"));
                FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
                p.size = 32;
                p.minFilter = TextureFilter.Linear;
                p.magFilter = TextureFilter.Linear;
                font = gen.generateFont(p);
                gen.dispose();

                nivel = 1;
                crearBloques(2 + nivel);
                uiViewport = new ScreenViewport();
                shape = new ShapeRenderer();
                pad = new Paddle(Gdx.graphics.getWidth()/2 - 50, 40, 100, 10); // <-- crea la paleta primero

                // Inicializa lista de pelotas ANTES de spawnear
                balls = new ArrayList<>();

                vidas = 3;
                puntaje = 0;

                // ahora sí puedes crear la primera pelota
                spawnBall(true);   
		}
		public void crearBloques(int filas) {
			blocks.clear();
			int blockWidth = 70;
		    int blockHeight = 26;
		    int y = Gdx.graphics.getHeight();
		    for (int cont = 0; cont<filas; cont++ ) {
		    	y -= blockHeight+10;
		    	for (int x = 5; x < Gdx.graphics.getWidth(); x += blockWidth + 10) {
		            blocks.add(new Block(x, y, blockWidth, blockHeight));
		        }
		    }
		}
                private void spawnBall(boolean attachToPaddle) {
                    int bx = Math.round(pad.getX() + pad.getWidth() / 2f - 5f);
                    int by = Math.round(pad.getY() + pad.getHeight() + 11f);
                    PingBall nb = new PingBall(bx, by, 10, 5, 7, attachToPaddle);
                    balls.add(nb);
                }
                private void resetGame() {
                mejorPuntaje = Math.max(mejorPuntaje, puntaje);
                puntaje = 0;
                nivel   = 1;
                vidas   = 3;
                nextThresholdIdx = 0;

                crearBloques(2 + nivel);
                balls.clear();
                spawnBall(true);}
                
		public void dibujaTextos() {
                    uiViewport.apply();  // aplica el viewport del HUD
                    batch.setProjectionMatrix(uiViewport.getCamera().combined);

                    batch.begin();
                    font.setColor(0, 0, 0, 0.7f);  // sombra
                    font.draw(batch, "Puntos: " + puntaje, 14, 38);
                    font.draw(batch, "Vidas : " + vidas, uiViewport.getWorldWidth() - 206, 38);

                    font.setColor(1, 1, 1, 1);     // texto principal
                    font.draw(batch, "Puntos: " + puntaje, 10, 40);
                    font.draw(batch, "Vidas : " + vidas, uiViewport.getWorldWidth() - 210, 40);
                    font.setColor(1, 1, 0.6f, 1);
                    font.draw(batch, "Mejor: " + mejorPuntaje, uiViewport.getWorldWidth()/2f - 70, 40);
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
                        b.update();
                    }
                    if (b.getY() < 0) fallen.add(b);
                }

                // remover caídas
                for (PingBall f : fallen) balls.remove(f);

                // si todas cayeron: perder vida y reponer 1 pegada (o reset si no quedan vidas)
                if (!fallen.isEmpty() && balls.isEmpty()) {
                    vidas--;
                    if (vidas > 0) {
                        spawnBall(true);
                    } else {
                        resetGame();
                        return; // salimos del render este frame
                    }
                }

                if (vidas <= 0) {
                    resetGame();
                }

                if (blocks.size() == 0) {
                    nivel++;
                    crearBloques(2 + nivel);
                    balls.clear();
                    spawnBall(true);
                }

                for (Block blk : blocks){
                    for (PingBall b : balls){
                        b.checkCollision(blk);
                    }
                }

                for (int i = 0; i < blocks.size(); i++) {
                    if (blocks.get(i).isDestroyed()) {
                        puntaje++; blocks.remove(i); i--;
                        if (nextThresholdIdx < extraBallAt.length && puntaje >= extraBallAt[nextThresholdIdx]) {
                            spawnBall(true);    // genera una nueva pelota pegada a la paleta
                            nextThresholdIdx++;}
                    }
                }

                for (PingBall b : balls){
                    b.checkCollision(pad);
                }

                // DIBUJO DE BLOQUES (SpriteBatch)
                camera.update();
                batch.setProjectionMatrix(camera.combined);
                batch.enableBlending();                     // <- asegura alpha habilitado
                batch.begin();
                for (Block b : blocks) {
                    // usa el color del bloque; si no existiera, fija uno
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
                shape.end();

                // HUD
                dibujaTextos();
            }
        @Override
        public void resize(int width, int height) {
            uiViewport.update(width, height, true);
        }
}
