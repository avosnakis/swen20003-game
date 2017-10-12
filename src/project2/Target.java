/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project2;

import java.util.ArrayList;

public class Target extends Tile {
  private boolean covered;

  public Target(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/target.png", "target", cellPosition, windowPosition);
    setPassable(true);

    covered = false;
  }

  @Override
  public void update(ArrayList<Integer> keys, int delta, World world) {
    covered = world.categoryAtLocation(getCellPosition(), "block");
  }

  public boolean isCovered() {
    return covered;
  }
}
