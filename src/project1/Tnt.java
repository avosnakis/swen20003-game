package project1;

public class Tnt extends Block {
  public Tnt(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/tnt.png", "tnt", cellPosition, windowPosition);
  }

  @Override
  public void moveToDestination(Direction direction, World world) {
    float speed = 32;
    int cellSpeed = 1;
    // Translate the direction to an x and y displacement
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

    int nextXCell = this.getxCell() + deltaXCell;
    int nextYCell = this.getyCell() + deltaYCell;

    // Destroy the cracked wall and the TNT if there is a cracked wall at the next location
    int crackedIndex = world.crackedWallAtLocation(nextXCell, nextYCell);
    if (crackedIndex != -1) {
      // Destroy the cracked wall and this tnt
      world.destroySprite(nextXCell, nextYCell, crackedIndex);
      world.destroySprite(this.getxCell(), this.getyCell(), this.getzCell());
      return;
    }

    this.addPastPosition(world.getTimer());
    world.setChangedThisFrame(true);

    // Make sure the position isn't occupied!
    if (!world.isBlocked(nextXCell, nextYCell, direction)) {
      world.moveIndex(this.getxCell(), this.getyCell(), this.getzCell(), nextXCell, nextYCell);

      this.setX(this.getX() + deltaX);
      this.setY(this.getY() + deltaY);

      this.setxCell(nextXCell);
      this.setyCell(nextYCell);
    }
  }
}
