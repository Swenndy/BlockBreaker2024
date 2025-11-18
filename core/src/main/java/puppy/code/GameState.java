package puppy.code;

public class GameState {

    private static  GameState instance;


    private int puntaje;
    private int mejorPuntaje;
    private int vidas;
    private int nivel;

    // --- 3) Constructor privado ---
    private GameState() {
        reset();
    }


    public static GameState get() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public void setMejorPuntaje(int mejorPuntaje) {
        this.mejorPuntaje = mejorPuntaje;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }


    public int getPuntaje() { return puntaje; }
    public void sumarPuntos(int p) { puntaje += p; }

    public int getMejorPuntaje() { return mejorPuntaje; }
    public void actualizarMejor() {
        mejorPuntaje = Math.max(mejorPuntaje, puntaje);
    }

    public int getVidas() { return vidas; }
    public void perderVida() { vidas--; }

    public int getNivel() { return nivel; }
    public void subirNivel() { nivel++; }


    public void reset() {
        puntaje = 0;
        vidas = 3;
        nivel = 1;
    }
}