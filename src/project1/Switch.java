package project1;

public class Switch extends Tile {
  public Switch(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/switch.png", "switch", cellPosition, windowPosition);
    super.setPassable(true);
  }
}
