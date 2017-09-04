package project1.tiles;

import project1.Sprite;

public class Stone extends Tile {
  public Stone(Sprite sprite, int xCell, int yCell) {
    super(sprite, xCell, yCell);
    this.setPassable(true);
  }
}
