package project1;

public abstract class Block extends Sprite implements Movable {
  public Block(String imageSource, float x, float y) {
    super(imageSource, "block", x, y);
    super.setPassable(true);
  }

  @Override
  public void moveToDestination(int direction) {

  }
}
