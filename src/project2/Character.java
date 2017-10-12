package project2;

public abstract class Character extends Sprite implements Movable {

  public Character(String imageSource, String type, Position<Integer> cellPosition, Position<Float> windowPosition) {
    super(imageSource, "character", type, cellPosition, windowPosition);
  }

  @Override
  public void moveToDestination(Direction direction, World world) {
    if (direction == Direction.DIR_NONE) {
      return;
    }

    float speed = 32;
    int cellSpeed = 1;
    // Translate the direction to an x and y displacement
    float deltaX = GameUtils.directionDelta('x', direction, speed);
    float deltaY = GameUtils.directionDelta('y', direction, speed);
    int deltaXCell = GameUtils.directionDelta('x', direction, cellSpeed);
    int deltaYCell = GameUtils.directionDelta('y', direction, cellSpeed);

    Position<Integer> nextPosition = new Position<>(getxCell() + deltaXCell, getyCell() + deltaYCell);

    float nextX = getX() + deltaX;
    float nextY = getY() + deltaY;

    // Make sure the position isn't occupied!
    if (!world.isBlocked(nextPosition, direction) && !world.categoryAtLocation(nextPosition, "block")) {
      setCellPosition(nextPosition);
      snapToGrid(nextX, nextY);
    }
  }

  @Override
  public void undo(int time) {
  }

  @Override
  public void addPastPosition(int time) {
  }
}