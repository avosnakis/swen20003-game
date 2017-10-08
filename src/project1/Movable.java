package project1;

public interface Movable {
  public void moveToDestination(Direction direction, World world);

  public void undo(int time);

  public void addPastPosition(int time);
}
