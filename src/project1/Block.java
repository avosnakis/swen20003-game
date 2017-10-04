package project1;

public abstract class Block extends Sprite implements Movable {
  public Block(String imageSource, String spriteType, Position<Integer> cellPosition, Position<Float> windowPosition) {
    super(imageSource, "block", spriteType, cellPosition, windowPosition);
    setPassable(true);
  }
}
