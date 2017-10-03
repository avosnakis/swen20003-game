/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

public class Target extends Tile {
  public Target(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/target.png", "target", cellPosition, windowPosition);
    super.setPassable(true);
  }
}
