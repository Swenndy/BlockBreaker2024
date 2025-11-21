/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package puppy.code;

/**
 *
 * @author simon
 */
public class HardLevelFactory implements BlockFactory {

    @Override
    public Block createNormal(int x, int y) {
        return new Block(x, y, 70, 26);
    }

    @Override
    public Block createHard(int x, int y) {
        return new BlockHard(x, y, 70, 26);
    }
}