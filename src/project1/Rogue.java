package project1;

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

    if (moveAttempts >= 2) {
      moveAttempts = 0;
      reverseDirection();
      return;
    }

    float speed = 32;
    int cellSpeed = 1;
    // Translate the direction to an x and y displacement
    float deltaX = 0;
    int deltaXCell = 0;

    switch (direction) {
      case DIR_LEFT:
        deltaX = -speed;
        deltaXCell = -cellSpeed;
        break;
      case DIR_RIGHT:
        deltaX = speed;
        deltaXCell = cellSpeed;
        break;
    }

    Position<Integer> nextPosition = new Position<>(getxCell() + deltaXCell, getyCell());

    float nextX = this.getX() + deltaX;

    // Restart the level if the player is there
    if (world.typeAtLocation(nextPosition, "player")) {
      world.reset();
    } else if (world.isBlocked(nextPosition, direction)) {
      moveAttempts++;
      reverseDirection();
      moveToDestination(currentDirection, world);
    } else {
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
  public void handlePlayerInput(Input input) {}

  @Override
  public void handlePlayerInput(ArrayList<Integer> keysPressed, World world) {
    Direction direction = GameUtils.playerMoved(keysPressed) ? currentDirection : Direction.DIR_NONE;
    moveToDestination(direction, world);
  }
}
