package project1;

public class Door extends Tile {
  public Door(float x, float y) {
    super("res/door.png", x, y);
    super.setPassable(false);
  }
}
