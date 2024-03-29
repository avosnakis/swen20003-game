package project2;

import org.newdawn.slick.Graphics;

import java.util.ArrayList;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Class for the TNT block.
 *
 * @author Alex Vosnakis 743936
 */
public class Tnt extends Block implements Destructible {

  private Notifier<CrackedWall> tntNotifier;
  private boolean undetonated;
  private Explosion explosion;

  public Tnt(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/tnt.png", "tnt", cellPosition, windowPosition);
    explosion = new Explosion(cellPosition, windowPosition);
    undetonated = false;
    tntNotifier = null;
  }

  /**
   * Update this sprite.
   *
   * @param keys  The keys the player has pressed.
   * @param delta The time since the last frame (ignored).
   * @param world The world this sprite is in.
   */
  @Override
  public void update(ArrayList<Integer> keys, int delta, World world) {
    // On the first frame, find the unique cracked wall and make it an observer
    if (tntNotifier == null) {
      tntNotifier = new Notifier<>((CrackedWall) world.findUniqueSprite("cracked"));
    }

    if (undetonated && !explosion.finishedExploding()) {
      explosion.increment(delta);
    } else if (explosion.finishedExploding()) {
      tntNotifier.alert();
    }
  }

  /**
   * If the Tnt has not exploded, the Tnt itself is rendered. If it's exploding, then the explosion is rendered.
   * Otherwise it is not rendered.
   *
   * @param g The Slick graphics object.
   */
  @Override
  public void render(Graphics g) {
    if (!undetonated) {
      super.render(g);
    } else if (!explosion.finishedExploding()) {
      explosion.render(g);
    }
  }

  @Override
  public void moveToDestination(Direction direction, World world) {
    if (undetonated) {
      return;
    }

    float speed = App.TILE_SIZE;
    int cellSpeed = GameUtils.CELL_SIZE;
    // Translate the direction to an x and y displacement
    float deltaX = GameUtils.directionDelta('x', direction, speed);
    float deltaY = GameUtils.directionDelta('y', direction, speed);
    int deltaXCell = GameUtils.directionDelta('x', direction, cellSpeed);
    int deltaYCell = GameUtils.directionDelta('y', direction, cellSpeed);

    Position<Integer> explosionPosition = new Position<>(getxCell() + deltaXCell, getyCell() + deltaYCell);

    // Destroy the cracked wall and the TNT if there is a cracked wall at the next location
    if (world.spriteAtLocation(explosionPosition, "cracked", Sprite.isOfType())) {
      setCellPosition(explosionPosition);
      snapToGrid(getX() + deltaX, getY() + deltaY);
      destroy(explosionPosition);
      return;
    }

    addPastPosition(world.getTimer());
    world.setChangedThisFrame(true);

    // Make sure the position isn't occupied!
    if (!world.isBlocked(explosionPosition, direction)) {
      setCellPosition(explosionPosition);
      snapToGrid(getX() + deltaX, getY() + deltaY);
    }
  }

  /**
   * Starts the undetonated animation, and destroys the CrackedWall at the target location.
   *
   * @param position The location of the CrackedWall and the explosion.
   */
  @Override
  public void destroy(Position<Integer> position) {
    undetonated = true;
    explosion.start(position);
  }

  @Override
  public void destroy() {
  }

  public boolean isUndetonated() {
    return undetonated;
  }
}
