/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

public class Wall extends Tile {
  public Wall(float x, float y, int xCell, int yCell) {
    super("res/wall.png", x, y, xCell, yCell);
    super.setPassable(false);
  }
}
