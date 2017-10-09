package project1;


import org.newdawn.slick.Input;

public class Skeleton extends Character {
  private static final int MOVE_TIME = 1000;
  private Timer timer;

  private Direction currentDirection;

  public Skeleton(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/skull.png", "skeleton", cellPosition, windowPosition);
    currentDirection = Direction.DIR_UP;

    timer = new Timer(MOVE_TIME);
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
    } else     if (world.categoryAtLocation(nextPosition, "block")) {
      reverseDirection();
      moveToDestination(currentDirection, world);
    }



    // Make sure the position isn't occupied!
    if (!world.isBlocked(nextPosition, direction)) {
      setCellPosition(nextPosition);
      snapToGrid(getX(), nextY);
    } else {
      reverseDirection();
      moveToDestination(currentDirection, world);
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
