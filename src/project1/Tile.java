package project1;

public abstract class Tile extends Sprite {
  public Tile(String imageSource, String spriteType, float x, float y, int xCell, int yCell) {
    super(imageSource, "tile", spriteType, x, y, xCell, yCell);
  }
}
