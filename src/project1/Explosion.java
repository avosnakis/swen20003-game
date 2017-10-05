package project1;

/**
 * Explosion effect created by TNT when it collides with a crackedwall.
 */
public class Explosion extends Sprite {
  private static final int EXPLOSION_TIME = 400;

  public Explosion(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/explosion.png", "effect", "explosion", cellPosition, windowPosition);
  }

  public void start(Position<Integer> explosionCell) {
    setCellPosition(explosionCell);

    float x = Loader.getOffsetX() + getxCell() * App.TILE_SIZE;
    float y = Loader.getOffsetY() + getyCell() * App.TILE_SIZE;
    snapToGrid(x, y);
  }
}
