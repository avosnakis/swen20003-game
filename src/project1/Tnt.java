package project1;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Tnt extends Block {

  private boolean exploding;
  private Explosion explosion;

  public Tnt(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/tnt.png", "tnt", cellPosition, windowPosition);
    explosion = new Explosion(cellPosition, windowPosition);
    exploding = false;
  }

  @Override
  public void update(Input input, int delta, World world) {
    if (exploding && !explosion.finishedExploding()) {
      explosion.increment(delta);
    } else if (explosion.finishedExploding()) {
      world.destroySprite(getCellPosition());
    }
  }

  @Override
  public void render(Graphics g) {
    if (!exploding) {
      super.render(g);
    } else if (!explosion.finishedExploding()) {
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
      explode(new Position<>(nextXCell, nextYCell, crackedIndex), world);
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

  /**
   * Starts the exploding animation, and destroys the CrackedWall at the target location.
   *
   * @param explosionLocation The location of the CrackedWall and the explosion.
   * @param world The world in which the explosion is occuring.
   */
  private void explode(Position<Integer> explosionLocation, World world) {
    exploding = true;
    world.destroySprite(explosionLocation);
    explosion.start(explosionLocation);
  }
}
