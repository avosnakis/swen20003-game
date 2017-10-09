package project1;

import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public class Tnt extends Block implements Destructible {

  private boolean exploding;
  private Explosion explosion;

  public Tnt(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/tnt.png", "tnt", cellPosition, windowPosition);

    explosion = new Explosion(cellPosition, windowPosition);
    exploding = false;
  }

  @Override
  public void update(ArrayList<Integer> keysPressed, int delta, World world) {
    if (exploding && !explosion.finishedExploding()) {
      explosion.increment(delta);
    } else if (explosion.finishedExploding()) {
      exploding = false;
      world.destroySprite(getCellPosition());
      world.destroySprite(explosion.getCellPosition());
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

    Position<Integer> explosionPosition = new Position<>(getxCell() + deltaXCell, getyCell() + deltaYCell);

    // Destroy the cracked wall and the TNT if there is a cracked wall at the next location
    if (world.typeAtLocation(explosionPosition, "cracked")) {
      explode(explosionPosition, deltaX, deltaY);
      return;
    }

    addPastPosition(world.getTimer());
    world.setChangedThisFrame(true);

    // Make sure the position isn't occupied!
    if (!world.isBlocked(explosionPosition, direction)) {
      setCellPosition(explosionPosition);
      snapToGrid(getX() + deltaX, getY()+ deltaY);
    }
  }

  /**
   * Starts the exploding animation, and destroys the CrackedWall at the target location.
   *
   * @param wallLocation The location of the CrackedWall and the explosion.
   * @param deltaX The x position in the window of the explosion.
   * @param deltaY The y position in the window of the explosion.
   */
  private void explode(Position<Integer> wallLocation, float deltaX, float deltaY) {
    setCellPosition(wallLocation);
    snapToGrid(getX() + deltaX, getY()+ deltaY);

    exploding = true;
    explosion.start(wallLocation);
  }

  public boolean isExploding() {
    return exploding;
  }
}
