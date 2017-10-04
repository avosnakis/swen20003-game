package project1;

import org.newdawn.slick.Input;

public class Ice extends Block {
  private static final int SLIDE_INTERVAL = 250;

  private int timer;
  private boolean sliding;
  private Direction currentSlideDirection;

  public Ice(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/ice.png", "ice", cellPosition, windowPosition);
    stop();
  }

  @Override
  public void update(Input input, int delta, World world) {

    // Increment the timer while it's sliding
    if (sliding) {
      timer += delta;
    }

    // Check if the ice block will slide in this frame, and if so, reset its timer as well
    if (timer >= SLIDE_INTERVAL && sliding) {
      moveToDestination(currentSlideDirection, world);
      timer = 0;
    }
  }

  @Override
  public void moveToDestination(Direction direction, World world) {
    float speed = 32;
    int cellSpeed = 1;
    // Translate the currentSlideDirection to an x and y displacement
    float deltaX = 0;
    float deltaY = 0;
    int deltaXCell = 0;
    int deltaYCell = 0;

    switch (direction) {
      case DIR_LEFT:
        deltaX = -speed;
        deltaXCell = -cellSpeed;
        break;
      case DIR_RIGHT:
        deltaX = speed;
        deltaXCell = cellSpeed;
        break;
      case DIR_UP:
        deltaY = -speed;
        deltaYCell = -cellSpeed;
        break;
      case DIR_DOWN:
        deltaY = speed;
        deltaYCell = cellSpeed;
        break;
    }

    if (direction == Direction.DIR_NONE) {
      return;
    }

    // It wasn't sliding when it moved, which means the player just pushed it, so store this position
    if (!sliding) {
      currentSlideDirection = direction;
      addPastPosition(world.getTimer());
      world.setChangedThisFrame(true);
    }

    int nextXCell = getxCell() + deltaXCell;
    int nextYCell = getyCell() + deltaYCell;

    // Make sure the position isn't occupied!
    if (!world.isBlocked(nextXCell, nextYCell, direction)) {
      world.moveReference(getCellPosition(), nextXCell, nextYCell);

      // Update the window coordinates
      snapToGrid(getX() + deltaX, getY() + deltaY);

      // The block is now sliding
      sliding = true;
    } else {
      // The ice block has collided and it can't move anymore
      stop();
    }
  }

  /**
   * Stops the ice block from sliding, and resets all associated parameters.
   */
  private void stop() {
    sliding = false;
    timer = 0;
    currentSlideDirection = Direction.DIR_NONE;
  }

  // TODO bug with undo interacting with Ice sliding. Throws an ArrayOutOfBoundsException. Reproducing is inconsistent.
  @Override
  public void undo(int time) {
    super.undo(time);
    stop();
  }
}
