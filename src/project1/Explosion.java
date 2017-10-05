package project1;

/**
 * Explosion effect created by TNT when it collides with a crackedwall.
 */
public class Explosion extends Sprite {
  private static final int EXPLOSION_TIME = 400;

  private int timer;

  public Explosion(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/explosion.png", "effect", "explosion", cellPosition, windowPosition);
    timer = 0;
  }

  public void start(Position<Integer> explosionCell) {
    setCellPosition(explosionCell);

    float x = Loader.getOffsetX() + getxCell() * App.TILE_SIZE;
    float y = Loader.getOffsetY() + getyCell() * App.TILE_SIZE;
    snapToGrid(x, y);
  }

  public void increment(int delta) {
    timer += delta;
  }

  public boolean finishedExploding() {
    return timer > EXPLOSION_TIME;
  }
}
