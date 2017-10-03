package project1;

import org.newdawn.slick.Input;

public class Ice extends Block {
  private static final int SLIDE_INTERVAL = 250;

  private int timer;
  private boolean sliding;
  private Direction currentSlideDirection;

  public Ice(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/ice.png", "ice", cellPosition, windowPosition);

    this.timer = 0;
    this.sliding = false;
    this.currentSlideDirection = Direction.DIR_NONE;
  }

  @Override
  public void update(Input input, int delta, World world) {

    // Increment the timer while it's sliding
    if (this.sliding) {
      this.timer += delta;
    }

    // Check if the ice block will slide in this frame, and if so, reset its timer as well
    if (this.timer >= SLIDE_INTERVAL && this.sliding) {
      this.moveToDestination(this.currentSlideDirection, world);
      this.timer = 0;
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
    if (!this.sliding) {
      this.currentSlideDirection = direction;
      this.addPastPosition(world.getTimer());
      world.setChangedThisFrame(true);
    }

    int nextXCell = this.getxCell() + deltaXCell;
    int nextYCell = this.getyCell() + deltaYCell;

    // Make sure the position isn't occupied!
    if (!world.isBlocked(nextXCell, nextYCell, direction)) {
      world.moveIndex(this.getxCell(),
          this.getyCell(),
          this.getzCell(),
          nextXCell,
          nextYCell);

      // Update the window coordinates
      this.setX(this.getX() + deltaX);
      this.setY(this.getY() + deltaY);

      // Update the cell coordinates
      this.setxCell(nextXCell);
      this.setyCell(nextYCell);

      // The block is now sliding
      this.sliding = true;
    } else {
      // The ice block has collided and it can't move anymore
      this.sliding = false;
      this.timer = 0;
      this.currentSlideDirection = Direction.DIR_NONE;
    }
  }
}
