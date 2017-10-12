package project2;

import java.util.ArrayList;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Template taken from:
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 * <p>
 * Class for the Target tile.
 *
 * @author Alex Vosnakis 743936
 */
public class Target extends Tile {
  private boolean covered;

  public Target(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/target.png", "target", cellPosition, windowPosition);
    setPassable(true);

    covered = false;
  }

  /**
   * Determine whether this target is covered by a Block.
   *
   * @param keys  The keys the player has pressed.
   * @param delta The time since the last frame (ignored).
   * @param world The world this sprite is in.
   */
  @Override
  public void update(ArrayList<Integer> keys, int delta, World world) {
    covered = world.categoryAtLocation(getCellPosition(), "block");
  }

  public boolean isCovered() {
    return covered;
  }
}
