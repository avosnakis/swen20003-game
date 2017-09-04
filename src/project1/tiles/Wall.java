package project1.tiles;

import project1.Sprite;

public class Wall extends Tile {
  public Wall(Sprite sprite, int xCell, int yCell) {
    super(sprite, xCell, yCell);
    this.setPassable(false);
  }
}
