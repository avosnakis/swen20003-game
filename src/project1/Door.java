package project1;

public class Door extends Tile {
  public Door(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/door.png", "door", cellPosition, windowPosition);
    setPassable(false);
  }
}
