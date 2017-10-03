/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

public class Wall extends Tile {
  public Wall(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/wall.png", "wall", cellPosition, windowPosition);
    super.setPassable(false);
  }
}
