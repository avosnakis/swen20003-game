/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

public class Target extends Tile {
  public Target(float x, float y, int xCell, int yCell) {
    super("res/Target.png", "target", x, y, xCell, yCell);
    super.setPassable(true);
  }
}
