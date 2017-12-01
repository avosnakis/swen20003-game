package project2;

import java.util.ArrayList;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Class for the Skeleton character.
 *
 * @author Alex Vosnakis 743936
 */
public class Skeleton extends Character {
  private static final int MOVE_TIME = 1000;
  private Timer timer;

  private Direction currentDirection;
  private int moveAttempts;

  public Skeleton(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/skull.png", "skeleton", cellPosition, windowPosition);
    currentDirection = Direction.DIR_UP;

    timer = new Timer(MOVE_TIME);
    moveAttempts = 0;
  }

  /**
   * If the Skeleton has gone over its movement timer, try to move it.
   *
   * @param keys  The keys the player has pressed.
   * @param delta The time since the last frame (ignored).
   * @param world The world this sprite is in.
   */
  @Override
  public void update(ArrayList<Integer> keys, int delta, World world) {
    timer.tick(delta);
    if (timer.reachedTimeout()) {
      moveToDestination(currentDirection, world);
      timer.reset();
    }
  }

  /**
   * Attempt to move the Skeleton. It reverses direction if it cannot move in that direction.
   *
   * @param direction The direction the Skeleton is attempting to move in.
   * @param world     The world the Skeleton is in.
   */
  @Override
  public void moveToDestination(Direction direction, World world) {
    // If the skeleton has attempted to turn around twice, it can't move so exit
    if (moveAttempts >= 2) {
      moveAttempts = 0;
      reverseDirection();
      return;
    }

    float speed = App.TILE_SIZE;
    int cellSpeed = GameUtils.CELL_SIZE;
    // Translate the direction to an x and y displacement
    float deltaY = GameUtils.directionDelta('y', direction, speed);
    int deltaYCell = GameUtils.directionDelta('y', direction, cellSpeed);

    Position<Integer> nextPosition = new Position<>(getxCell(), getyCell() + deltaYCell);

    float nextY = getY() + deltaY;

    // Restart the level if the player is there
    if (world.spriteAtLocation(nextPosition, "player", Sprite.isOfType())) {
      world.reset();
    } else if (world.spriteAtLocation(nextPosition, "block", Sprite.isOfCategory()) ||
            world.isBlocked(nextPosition, direction)) {
      moveAttempts++;
      reverseDirection();
      moveToDestination(currentDirection, world);
    } else {
      moveAttempts = 0;
      setCellPosition(nextPosition);
      snapToGrid(getX(), nextY);
    }
  }

  /**
   * Reverses the skeleton's current direction.
   * If it is going down, its direction is now up.
   * If it is going up, its direction is now down.
   */
  private void reverseDirection() {
    if (currentDirection == Direction.DIR_DOWN) {
      currentDirection = Direction.DIR_UP;
    } else {
      currentDirection = Direction.DIR_DOWN;
    }
  }

  /**
   * Skeleton can't undo.
   *
   * @param time Timestamp of undo (ignored)
   */
  @Override
  public void undo(int time) {
  }
}
