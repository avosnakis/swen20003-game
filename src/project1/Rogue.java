package project1;

import org.newdawn.slick.Input;

import java.util.ArrayList;

public class Rogue extends Character {

  private Direction currentDirection;
  private int moveAttempts;

  public Rogue(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/rogue.png", "rogue", cellPosition, windowPosition);

    currentDirection = Direction.DIR_LEFT;
    moveAttempts = 0;
  }

  @Override
  public void update(ArrayList<Integer> arrowKeys, int delta, World world) {
    Direction direction = playerMoved(arrowKeys) ? currentDirection : Direction.DIR_NONE;
    moveToDestination(direction, world);
  }

  @Override
  public void moveToDestination(Direction direction, World world) {
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

  private void reverseDirection() {
    if (currentDirection == Direction.DIR_LEFT) {
      currentDirection = Direction.DIR_RIGHT;
    } else {
      currentDirection = Direction.DIR_LEFT;
    }
  }

  private static boolean playerMoved(ArrayList<Integer> keysPressed) {
    return keysPressed.size() > 0;
  }

}
