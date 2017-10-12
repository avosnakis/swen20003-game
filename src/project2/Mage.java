package project2;

import org.newdawn.slick.Input;

import java.util.ArrayList;

public class Mage extends Character implements Controllable {
  public Mage(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/mage.png", "mage", cellPosition, windowPosition);
  }

  @Override
  public void update(ArrayList<Integer> keysPressed, int delta, World world) {
    handlePlayerInput(keysPressed, world);
  }

  @Override
  public void moveToDestination(Direction direction, World world) {
    super.moveToDestination(direction, world);
  }

  @Override
  public void handlePlayerInput(Input input) {
  }

  @Override
  public void handlePlayerInput(ArrayList<Integer> keysPressed, World world) {
    Direction direction = Direction.DIR_NONE;

    if (GameUtils.playerMoved(keysPressed)) {
      direction = determineDirection(world.getPlayerPosition());
    }

    moveToDestination(direction, world);
  }

  /**
   * Determines the direction the Mage will move in according to Algorithm 1.
   *
   * @param playerPosition The position of the player.
   * @return The direction the mage needs to go in.
   */
  public Direction determineDirection(Position<Integer> playerPosition) {
    if (playerPosition == null) {
      return Direction.DIR_NONE;
    }

    int distX = playerPosition.x - getxCell();
    int distY = playerPosition.y - getyCell();

    if (Math.abs(distX) > Math.abs(distY)) {
      return distX < 0 ? Direction.DIR_LEFT : Direction.DIR_RIGHT;
    } else {
      return distX < 0 ? Direction.DIR_UP : Direction.DIR_DOWN;
    }
  }
}
