/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

public class Floor extends Tile {
  public Floor(float x, float y) {
    super("res/floor.png", x, y);
    super.setPassable(true);
  }
}
