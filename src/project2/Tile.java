package project2;

public abstract class Tile extends Sprite {
  public Tile(String imageSource, String type, Position<Integer> cellPosition, Position<Float> windowPosition) {
    super(imageSource, "tile", type, cellPosition, windowPosition);
  }
}
