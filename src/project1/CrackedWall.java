package project1;

public class CrackedWall extends Tile implements Destructible {
  public CrackedWall(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/cracked_wall.png", "cracked", cellPosition, windowPosition);
    setPassable(false);
  }
}
