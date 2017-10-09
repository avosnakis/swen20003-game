package project1;

import java.util.ArrayList;

public class Ice extends Block {
  private static final int SLIDE_INTERVAL = 250;

  private Timer timer;
  private boolean sliding;
  private Direction currentSlideDirection;

  public Ice(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/ice.png", "ice", cellPosition, windowPosition);

    timer = new Timer(SLIDE_INTERVAL);
    stop();
  }

  @Override
  public void update(ArrayList<Integer> keysPressed, int delta, World world) {

    // Increment the timer while it's sliding
    if (sliding) {
      timer.increment(delta);
    }

    // Check if the ice block will slide in this frame, and if so, reset its timer as well
    if (timer.reachedTimeout() && sliding) {
      moveToDestination(currentSlideDirection, world);
      timer.reset();
    }
  }

  // TODO seems to be a bug with ice interaction with player
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

    Position<Integer> nextPosition = new Position<>(getxCell() + deltaXCell, getyCell() + deltaYCell);

    // Make sure the position isn't occupied!
    if (!world.isBlocked(nextPosition, direction)) {
      setCellPosition(nextPosition);
      snapToGrid(getX() + deltaX, getY() + deltaY);

      // The block is now sliding
      sliding = true;
      currentSlideDirection = direction;
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
    timer.reset();
    currentSlideDirection = Direction.DIR_NONE;
  }

  @Override
  public void undo(int time) {
    super.undo(time);
    stop();
  }
}
