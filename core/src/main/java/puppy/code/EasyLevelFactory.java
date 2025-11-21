package puppy.code;

/**
 *
 * @author simon
 */
public class EasyLevelFactory implements BlockFactory {

    @Override
    public Block createNormal(int x, int y) {
        return new Block(x, y, 70, 26);
    }

    @Override
    public Block createHard(int x, int y) {
        
        return new Block(x, y, 70, 26);
    }
}