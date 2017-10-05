package project1;

import org.newdawn.slick.Graphics;

public class Tnt extends Block {
  private boolean exploding;
  private Explosion explosion;

  public Tnt(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/tnt.png", "tnt", cellPosition, windowPosition);
    explosion = new Explosion(cellPosition, windowPosition);
    exploding = false;
  }

  @Override
  public void render(Graphics g) {
    if (!exploding) {
      super.render(g);
    } else {
      explosion.render(g);
    }
  }

  @Override
  public void moveToDestination(Direction direction, World world) {
    if (exploding) {
      return;
    }

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

    int nextXCell = getxCell() + deltaXCell;
    int nextYCell = getyCell() + deltaYCell;

    // Destroy the cracked wall and the TNT if there is a cracked wall at the next location
    int crackedIndex = world.crackedWallAtLocation(nextXCell, nextYCell);
    if (crackedIndex != WorldState.NO_INDEX) {
      explode(nextXCell, nextYCell, crackedIndex, world);
      return;
    }

    addPastPosition(world.getTimer());
    world.setChangedThisFrame(true);

    // Make sure the position isn't occupied!
    if (!world.isBlocked(nextXCell, nextYCell, direction)) {
      world.moveReference(getCellPosition(), nextXCell, nextYCell);
      snapToGrid(getX() + deltaX, getY()+ deltaY);
    }
  }

  private void explode(int nextXCell, int nextYCell, int nextZCell, World world) {
    exploding = true;
    Position<Integer> explosionCell = new Position<>(nextXCell, nextYCell, nextZCell);
    world.destroySprite(explosionCell);
    explosion.start(explosionCell);
  }
}
