package project1;

public abstract class Block extends Sprite implements Movable {
  public Block(String imageSource, float x, float y) {
    super(imageSource, x, y);
  }

  @Override
  public void moveToDestination(int direction) {

  }
}
