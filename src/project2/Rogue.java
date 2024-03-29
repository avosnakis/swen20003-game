package project2;

import java.util.ArrayList;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Class for the Rogue character.
 *
 * @author Alex Vosnakis 743936
 */
public class Rogue extends Character implements Controllable {

  private Direction currentDirection;
  private int moveAttempts;

  public Rogue(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/rogue.png", "rogue", cellPosition, windowPosition);
    currentDirection = Direction.DIR_LEFT;
    moveAttempts = 0;
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

  /**
   * Try to move this Rogue.
   *
   * @param direction The direction the Rogue is attempting to move in.
   * @param world     The world the Rogue is in.
   */
  @Override
  public void moveToDestination(Direction direction, World world) {
    if (direction == Direction.DIR_NONE) {
      return;
    }

    // If it has attempted to move twice, it can't move.
    if (moveAttempts >= 2) {
      moveAttempts = 0;
      reverseDirection();
      return;
    }

    float speed = App.TILE_SIZE;
    int cellSpeed = GameUtils.CELL_SIZE;
    // Translate the direction to an x displacement
    float deltaX = GameUtils.directionDelta('x', direction, speed);
    int deltaXCell = GameUtils.directionDelta('x', direction, cellSpeed);

    Position<Integer> nextPosition = new Position<>(getxCell() + deltaXCell, getyCell());
    float nextX = getX() + deltaX;

    // Restart the level if the player is there
    if (world.spriteAtLocation(nextPosition, "player", Sprite.isOfType())) {
      world.reset();
    } else if (world.isBlocked(nextPosition, direction)) {
      moveAttempts++;
      reverseDirection();
      moveToDestination(currentDirection, world);
    } else {
      world.moveBlockAtPosition(nextPosition, direction);
      setCellPosition(nextPosition);
      snapToGrid(nextX, getY());
    }
  }

  /**
   * Reverses the rogue's current direction.
   * If it is going left, its direction is now right.
   * If it is going right, its direction is now left.
   */
  private void reverseDirection() {
    if (currentDirection == Direction.DIR_LEFT) {
      currentDirection = Direction.DIR_RIGHT;
    } else {
      currentDirection = Direction.DIR_LEFT;
    }
  }

  @Override
  public void handlePlayerInput(ArrayList<Integer> input) {
  }

  /**
   * Determine what direction the Rogue is trying to move in, and try to move there.
   *
   * @param keysPressed The keys the player has pressed.
   * @param world       The world the Rogue is in.
   */
  @Override
  public void handlePlayerInput(ArrayList<Integer> keysPressed, World world) {
    Direction direction = GameUtils.playerMoved(keysPressed) ? currentDirection : Direction.DIR_NONE;
    moveToDestination(direction, world);
  }

  /**
   * Rogue can't undo.
   *
   * @param time Timestamp of undo (ignored)
   */
  @Override
  public void undo(int time) {
  }
}
