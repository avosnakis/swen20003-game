package project1;

public class CrackedWall extends Tile {
  public CrackedWall(float x, float y) {
    super("res/cracked_wall.png", x, y);
    super.setPassable(false);
  }
}
