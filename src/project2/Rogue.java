package project2;

import org.newdawn.slick.Input;

import java.util.ArrayList;

public class Rogue extends Character implements Controllable {

  private Direction currentDirection;
  private int moveAttempts;

  public Rogue(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/rogue.png", "rogue", cellPosition, windowPosition);

    currentDirection = Direction.DIR_LEFT;
    moveAttempts = 0;
  }

  @Override
  public void update(ArrayList<Integer> keysPressed, int delta, World world) {
    handlePlayerInput(keysPressed, world);
  }

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
    if (world.typeAtLocation(nextPosition, "player")) {
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
  public void handlePlayerInput(Input input) {
  }

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
