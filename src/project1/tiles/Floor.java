package project1.tiles;

import project1.Sprite;

public class Floor extends Tile {
  public Floor(Sprite sprite, int xCell, int yCell) {
    super(sprite, xCell, yCell);
    this.setPassable(true);
  }
}
