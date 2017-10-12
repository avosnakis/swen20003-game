package project2;

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
  public void update(ArrayList<Integer> keys, int delta, World world) {

    // Increment the timer while it's sliding
    if (sliding) {
      timer.tick(delta);
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
    float speed = App.TILE_SIZE;
    int cellSpeed = GameUtils.CELL_SIZE;
    // Translate the currentSlideDirection to an x and y displacement
    float deltaX = GameUtils.directionDelta('x', direction, speed);
    float deltaY = GameUtils.directionDelta('y', direction, speed);
    int deltaXCell = GameUtils.directionDelta('x', direction, cellSpeed);
    int deltaYCell = GameUtils.directionDelta('y', direction, cellSpeed);

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
    if (!world.isBlocked(nextPosition, direction) && !world.categoryAtLocation(nextPosition, "block")) {
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
