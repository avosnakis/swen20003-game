package project1;

public abstract class Block extends Sprite implements Movable {
  public Block(String imageSource, String spriteType, float x, float y, int xCell, int yCell) {
    super(imageSource, "block", spriteType, x, y, xCell, yCell);
    super.setPassable(true);
  }
}
