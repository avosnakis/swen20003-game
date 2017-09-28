/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

public class Floor extends Tile {
  public Floor(float x, float y, int xCell, int yCell) {
    super("res/floor.png", "floor", x, y, xCell, yCell);
    super.setPassable(true);
  }
}
