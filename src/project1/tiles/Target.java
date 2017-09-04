package project1.tiles;

import project1.Sprite;

/**
 * Class for the target tile.
 */
public class Target extends Tile {
  public Target(Sprite sprite, int xCell, int yCell) {
    super(sprite, xCell, yCell);
    this.setPassable(true);
  }
}
