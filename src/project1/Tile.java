package project1;

public abstract class Tile extends Sprite {
  public Tile(String imageSource, float x, float y, int xCell, int yCell) {
    super(imageSource, "tile", x, y, xCell, yCell);
  }
}
