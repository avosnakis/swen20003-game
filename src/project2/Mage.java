package project2;

import java.util.ArrayList;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Class for the Mage character. Implements Algorithm 1 as described in the spec for its movement.
 *
 * @author Alex Vosnakis 743936
 */
public class Mage extends Character implements Controllable {

  public Mage(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/mage.png", "mage", cellPosition, windowPosition);
  }

  /**
   * Update this sprite.
   *
   * @param keys  The keys the player has pressed.
   * @param delta The time since the last frame (ignored).
   * @param world The world this sprite is in.
   */
  @Override
  public void update(ArrayList<Integer> keys, int delta, World world) {
    handlePlayerInput(keys, world);
  }

  @Override
  public void handlePlayerInput(ArrayList<Integer> input) {
  }

  /**
   * If the player has moved, determine which direction to move in and attempt to move in that direction.
   *
   * @param keysPressed The arrow keys the player has pressed.
   * @param world       The world the mage is in.
   */
  @Override
  public void handlePlayerInput(ArrayList<Integer> keysPressed, World world) {
    Direction direction = Direction.DIR_NONE;

    if (GameUtils.playerMoved(keysPressed)) {
      direction = determineDirection(world.getPlayerPosition());
    }

    moveToDestination(direction, world);
  }

  /**
   * Determines the direction the Mage will move in according to Algorithm 1.
   *
   * @param playerPosition The position of the player.
   * @return The direction the mage needs to go in.
   */
  private Direction determineDirection(Position<Integer> playerPosition) {
    if (playerPosition == null) {
      return Direction.DIR_NONE;
    }

    float distX = Loader.cellToWindow(playerPosition.x, 'x') - getX();
    float distY = Loader.cellToWindow(playerPosition.y, 'y') - getY();

    if (Math.abs(distX) > Math.abs(distY)) {
      return distX < 0 ? Direction.DIR_LEFT : Direction.DIR_RIGHT;
    } else {
      return distX < 0 ? Direction.DIR_UP : Direction.DIR_DOWN;
    }
  }

  /**
   * Mage can't undo.
   *
   * @param time Timestamp of undo (ignored)
   */
  @Override
  public void undo(int time) {
  }
}
