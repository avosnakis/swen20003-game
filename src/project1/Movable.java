package project1;

public interface Movable {
  int DIR_NONE = 0;
  int DIR_LEFT = 1;
  int DIR_RIGHT = 2;
  int DIR_UP = 3;
  int DIR_DOWN = 4;

  public void moveToDestination(int direction, World world);
}
