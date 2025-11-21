package puppy.code;

/**
 *
 * @author simon
 */
public interface BlockFactory {
    Block createNormal(int x, int y);
    Block createHard(int x, int y);
}
