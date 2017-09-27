/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

public class Wall extends Tile {
  public Wall(float x, float y) {
    super("res/wall.png", x, y);
    super.setPassable(false);
  }
}
