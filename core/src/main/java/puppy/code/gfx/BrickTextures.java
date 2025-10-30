package puppy.code.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

public class BrickTextures {

    // Clave para el caché, compatible con Java 11
    private static final class Key {
        final int w, h, radius, rgba;
        Key(int w, int h, int radius, int rgba) {
            this.w = w; this.h = h; this.radius = radius; this.rgba = rgba;
        }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;
            Key k = (Key) o;
            return w == k.w && h == k.h && radius == k.radius && rgba == k.rgba;
        }
        @Override public int hashCode() {
            int r = 17;
            r = 31*r + w;
            r = 31*r + h;
            r = 31*r + radius;
            r = 31*r + rgba;
            return r;
        }
    }

    private final Map<Key, Texture> cache = new HashMap<>();

    public Texture get(int w, int h, int radius, Color base) {
        Key k = new Key(w, h, radius, Color.rgba8888(base));
        Texture t = cache.get(k);
        if (t == null) {
            t = makeBrickTexture(w, h, base, radius);
            cache.put(k, t);
        }
        return t;
    }

    private static Texture makeBrickTexture(int w, int h, Color base, int r) {
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);

        // fondo transparente
        pm.setColor(0, 0, 0, 0); pm.fill();

        // sombra desplazada
        pm.setColor(0, 0, 0, 0.22f);
        roundedRect(pm, 2, 2, w - 2, h - 2, r);

        // gradiente del cuerpo
        Color top = new Color(base).lerp(Color.WHITE, 0.22f);
        Color bot = new Color(base).lerp(Color.BLACK, 0.18f);
        for (int y = 0; y < h; y++) {
            float t = (h == 1) ? 0f : (float) y / (h - 1);
            float rr = top.r * (1 - t) + bot.r * t;
            float gg = top.g * (1 - t) + bot.g * t;
            float bb = top.b * (1 - t) + bot.b * t;
            pm.setColor(rr, gg, bb, 1f);
            for (int x = 0; x < w; x++) if (insideRounded(x, y, w, h, r)) pm.drawPixel(x, y);
        }

        // brillo superior
        pm.setColor(1, 1, 1, 0.16f);
        int bh = Math.max(2, (int) (h * 0.25f));
        roundedRect(pm, 1, h - bh - 1, w - 2, bh, Math.max(1, r - 2));

        // borde
        pm.setColor(0, 0, 0, 0.55f);
        roundedStroke(pm, 0, 0, w, h, r);

        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    private static boolean insideRounded(int x, int y, int w, int h, int r) {
        if (r <= 0) return x >= 0 && x < w && y >= 0 && y < h;
        if ((x >= r && x < w - r) || (y >= r && y < h - r)) return true;
        int dx, dy;
        dx = x - r;          dy = y - r;           if (dx*dx + dy*dy <= r*r) return true;              // abajo-izq
        dx = x - (w-1-r);    dy = y - r;           if (dx*dx + dy*dy <= r*r) return true;              // abajo-der
        dx = x - r;          dy = y - (h-1-r);     if (dx*dx + dy*dy <= r*r) return true;              // arriba-izq
        dx = x - (w-1-r);    dy = y - (h-1-r);     return dx*dx + dy*dy <= r*r;                         // arriba-der
    }

    private static void roundedRect(Pixmap pm, int x, int y, int w, int h, int r) {
        for (int yy = 0; yy < h; yy++)
            for (int xx = 0; xx < w; xx++)
                if (insideRounded(xx, yy, w, h, r)) pm.drawPixel(x + xx, y + yy);
    }

    private static void roundedStroke(Pixmap pm, int x, int y, int w, int h, int r) {
        for (int i = 0; i < w; i++) { pm.drawPixel(x + i, y); pm.drawPixel(x + i, y + h - 1); }
        for (int j = 0; j < h; j++) { pm.drawPixel(x, y + j); pm.drawPixel(x + w - 1, y + j); }
    }

    public void dispose() {
        for (Texture t : cache.values()) t.dispose();
        cache.clear();
    }
}