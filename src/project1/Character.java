package project1;

public abstract class Character extends Sprite implements Movable {
  public Character(String imageSource, String spriteType, float x, float y, int xCell, int yCell) {
    super(imageSource, "character", spriteType, x, y, xCell, yCell);
  }
}