/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

public class Floor extends Tile {
  public Floor(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/floor.png", "floor", cellPosition, windowPosition);
    setPassable(true);
  }
}