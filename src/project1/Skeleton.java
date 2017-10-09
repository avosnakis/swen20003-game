package project1;


import org.newdawn.slick.Input;

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
  public void update(Input input, int delta, World world) {
    timer.increment(delta);
    if (timer.reachedTimeout()) {
      moveToDestination(currentDirection, world);
      timer.reset();
    }
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
    float deltaY = 0;
    int deltaYCell = 0;

    switch (direction) {
      case DIR_UP:
        deltaY = -speed;
        deltaYCell = -cellSpeed;
        break;
      case DIR_DOWN:
        deltaY = speed;
        deltaYCell = cellSpeed;
        break;
    }

    addPastPosition(world.getTimer());
    world.setChangedThisFrame(true);

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

  private void reverseDirection() {
    if (currentDirection == Direction.DIR_DOWN) {
      currentDirection = Direction.DIR_UP;
    } else {
      currentDirection = Direction.DIR_DOWN;
    }
  }
}
