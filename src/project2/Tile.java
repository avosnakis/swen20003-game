package project2;

public abstract class Tile extends Sprite {
  public Tile(String imageSource, String spriteType, Position<Integer> cellPosition, Position<Float> windowPosition) {
    super(imageSource, "tile", spriteType, cellPosition, windowPosition);
  }
}
