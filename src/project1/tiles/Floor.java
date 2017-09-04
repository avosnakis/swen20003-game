package project1.tiles;

import project1.Sprite;


/**
 * Class for the floor tile.
 */
public class Floor extends Tile {

  /**
   * Instantiates a floor.
   *
   * @param sprite This floor's sprite.
   * @param xCell The x-coordinate in the world of this cell.
   * @param yCell The y-coordinate in the world of this cell.
   */

  public Floor(Sprite sprite, int xCell, int yCell) {
    super(sprite, xCell, yCell);
    this.setPassable(true);
  }
}
