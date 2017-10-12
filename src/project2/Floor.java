package project2;

/**
 * SWEN20003 Assignment 2B
 *
 * Template taken from:
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 *
 * Class for the Floor tile.
 *
 * @author Alex Vosnakis 743936
 */
public class Floor extends Tile {
  public Floor(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/floor.png", "floor", cellPosition, windowPosition);
    setPassable(true);
  }
}