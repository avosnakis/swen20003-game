package project1;


/**
 * Class for the wall tile.
 */
public class Wall extends Tile {

  /**
   * Instantiates a wall.
   *
   * @param sprite This wall's sprite.
   * @param xCell  The x-coordinate in the world of this cell.
   * @param yCell  The y-coordinate in the world of this cell.
   */
  public Wall(Sprite sprite, int xCell, int yCell) {
    super(sprite, xCell, yCell);
    this.setPassable(false);
  }
}
