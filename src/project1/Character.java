package project1;

public abstract class Character extends Sprite implements Movable {
  public Character(String imageSource, String spriteType, Position<Integer> cellPosition, Position<Float> windowPosition) {
    super(imageSource, "character", spriteType, cellPosition, windowPosition);
  }
}