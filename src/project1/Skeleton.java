package project1;


import java.util.ArrayList;

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

  @Override
  public void update(ArrayList<Integer> keysPressed, int delta, World world) {
    timer.tick(delta);
    if (timer.reachedTimeout()) {
      moveToDestination(currentDirection, world);
      timer.reset();
    }
  }

  @Override
  public void moveToDestination(Direction direction, World world) {
    // If the skeleton has attempted to turn around twice, it can't move so exit
    if (moveAttempts >= 2) {
      moveAttempts = 0;
      reverseDirection();
      return;
    }

    float speed = 32;
    int cellSpeed = 1;
    // Translate the direction to an x and y displacement
    float deltaY = GameUtils.directionDelta('y', direction, speed);
    int deltaYCell = GameUtils.directionDelta('y', direction, cellSpeed);

    Position<Integer> nextPosition = new Position<>(getxCell(), getyCell() + deltaYCell);

    float nextY = this.getY() + deltaY;

    // Restart the level if the player is there
    if (world.typeAtLocation(nextPosition, "player")) {
      world.reset();
    } else if (world.categoryAtLocation(nextPosition, "block") || world.isBlocked(nextPosition, direction)) {
      moveAttempts++;
      reverseDirection();
      moveToDestination(currentDirection, world);
    } else {
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
}
