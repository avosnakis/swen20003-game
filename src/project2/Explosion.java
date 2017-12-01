package project2;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Explosion effect created by TNT when it collides with a CrackedWall.
 *
 * @author Alex Vosnakis 743936
 */
public class Explosion extends Sprite {
  private static final int EXPLOSION_TIME = 400;

  private Timer timer;

  public Explosion(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/explosion.png", "effect", "explosion", cellPosition, windowPosition);
    timer = new Timer(EXPLOSION_TIME);
  }

  /**
   * Sets the explosion to the appropriate position.
   *
   * @param explosionCell
   */
  public void start(Position<Integer> explosionCell) {
    setCellPosition(explosionCell);

    float x = Loader.getOffsetX() + getxCell() * App.TILE_SIZE;
    float y = Loader.getOffsetY() + getyCell() * App.TILE_SIZE;
    snapToGrid(x, y);
  }

  public void increment(int delta) {
    timer.tick(delta);
  }

  public boolean finishedExploding() {
    return timer.reachedTimeout();
  }
}
