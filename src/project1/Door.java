package project1;

public class Door extends Tile {
  public Door(float x, float y, int xCell, int yCell) {
    super("res/door.png", x, y, xCell, yCell);
    super.setPassable(false);
  }
}
