/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

public class Stone extends Block {
  public Stone(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/stone.png", "stone", cellPosition, windowPosition);
  }
}
