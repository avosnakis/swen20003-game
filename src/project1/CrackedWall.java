package project1;

public class CrackedWall extends Tile {
  public CrackedWall(float x, float y, int xCell, int yCell) {
    super("res/cracked_wall.png", "cracked", x, y, xCell, yCell);
    super.setPassable(false);
  }
}
