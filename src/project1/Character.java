package project1;

public abstract class Character extends Sprite implements Movable {
  public Character(String imageSource, float x, float y, int xCell, int yCell) {
    super(imageSource, "character", x, y, xCell, yCell);
  }
}