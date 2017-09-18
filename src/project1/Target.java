package project1;


/**
 * Class for the target tile.
 */
public class Target extends Tile {

  /**
   * Instantiates a target.
   *
   * @param sprite This target's sprite.
   * @param xCell The x-coordinate in the world of this cell.
   * @param yCell The y-coordinate in the world of this cell.
   */
  public Target(Sprite sprite, int xCell, int yCell) {
    super(sprite, xCell, yCell);
    this.setPassable(true);
  }
}
