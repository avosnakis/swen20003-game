package project2;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Template taken from:
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 * <p>
 * Abstract class for all tiles.
 *
 * @author Alex Vosnakis 743936
 */
public abstract class Tile extends Sprite {
  public Tile(String imageSource, String type, Position<Integer> cellPosition, Position<Float> windowPosition) {
    super(imageSource, "tile", type, cellPosition, windowPosition);
  }
}
