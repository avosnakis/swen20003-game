package project2;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Template taken from:
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 * <p>
 * Class for the Floor tile.
 *
 * @author Alex Vosnakis 743936
 */
public class Stone extends Block {
  public Stone(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/stone.png", "stone", cellPosition, windowPosition);
  }
}
