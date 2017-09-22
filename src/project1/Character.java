package project1;

public abstract class Character extends Sprite implements Movable {
  public Character(String imageSource, float x, float y) {
    super("res/player_left.png", x, y);
  }

  @Override
  public void moveToDestination(int direction) {
    float speed = 32;
    // Translate the direction to an x and y displacement
    float deltaX = 0;
    float deltaY = 0;
    switch (direction) {
      case DIR_LEFT:
        deltaX = -speed;
        break;
      case DIR_RIGHT:
        deltaX = speed;
        break;
      case DIR_UP:
        deltaY = -speed;
        break;
      case DIR_DOWN:
        deltaY = speed;
        break;
    }

    // Make sure the position isn't occupied!
    if (!Loader.isBlocked(this.getX() + deltaX, this.getY() + deltaY)) {
      this.setX(this.getX() + deltaX);
      this.setY(this.getY() + deltaY);
    }
  }


}
