package project1;

public class Switch extends Tile {
  public Switch(float x, float y, int xCell, int yCell) {
    super("res/switch.png", "switch", x, y, xCell, yCell);
    super.setPassable(true);
  }
}
