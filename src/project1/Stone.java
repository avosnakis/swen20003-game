package project1;


/**
 * Class for the stone tile.
 */
public class Stone extends Tile {

  /**
   * Instantiates a stone.
   *
   * @param sprite This stone's sprite.
   * @param xCell  The x-coordinate in the world of this cell.
   * @param yCell  The y-coordinate in the world of this cell.
   */
  public Stone(Sprite sprite, int xCell, int yCell) {
    super(sprite, xCell, yCell);
    this.setPassable(true);
  }
}
