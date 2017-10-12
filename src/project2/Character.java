package project2;

/**
 * Superclass for all characters, ie. the player and the enemies.
 *
 * @author Alex Vosnakis 743946
 */
public abstract class Character extends Sprite implements Movable {

  public Character(String imageSource, String type, Position<Integer> cellPosition, Position<Float> windowPosition) {
    super(imageSource, "character", type, cellPosition, windowPosition);
    setPassable(false);
  }

  /**
   * Attempt to move the player in the specified direction in the world. If it runs into a block, it will also
   * attempt to move that.
   *
   * @param direction The direction the player is attempting to move in.
   * @param world     The world the Player is in.
   */
  @Override
  public void moveToDestination(Direction direction, World world) {
    if (direction == Direction.DIR_NONE) {
      return;
    }

    float speed = App.TILE_SIZE;
    int cellSpeed = GameUtils.CELL_SIZE;
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

  /**
   * Not all Characters can undo, so this is empty.
   *
   * @param time The time to revert back to (ignored)
   */
  @Override
  public void undo(int time) {
  }

  /**
   * Not all Characters can undo, so this is empty.
   *
   * @param time The time to record (ignored)
   */
  @Override
  public void addPastPosition(int time) {
  }
}