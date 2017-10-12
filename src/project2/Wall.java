package project2;

/**
 * SWEN20003 Assignment 2B
 *
 * Template taken from:
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 *
 * Class for the Wall tile.
 *
 * @author Alex Vosnakis 743936
 */
public class Wall extends Tile {
  public Wall(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/wall.png", "wall", cellPosition, windowPosition);
    setPassable(false);
  }
}
