/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

import org.newdawn.slick.Input;

public class Player extends Character implements Controllable {

  public Player(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/player_left.png", "player", cellPosition, windowPosition);
  }

  @Override
  public void update(Input input, int delta, World world) {
    handlePlayerInput(input, world);
  }

  @Override
  public void handlePlayerInput(Input input) {
  }

  @Override
  public void handlePlayerInput(Input input, World world) {
    Direction direction = Direction.determineDirection(input);
    if (direction == Direction.DIR_NONE) {
      return;
    }

    world.incrementMoves();
    // Move to our destination
    moveToDestination(direction, world);
  }

  @Override
  public void moveToDestination(Direction direction, World world) {
    float speed = 32;
    int cellSpeed = 1;
    // Translate the direction to an x and y displacement
    float deltaX = 0;
    float deltaY = 0;
    int deltaXCell = 0;
    int deltaYCell = 0;

    switch (direction) {
      case DIR_LEFT:
        deltaX = -speed;
        deltaXCell = -cellSpeed;
        break;
      case DIR_RIGHT:
        deltaX = speed;
        deltaXCell = cellSpeed;
        break;
      case DIR_UP:
        deltaY = -speed;
        deltaYCell = -cellSpeed;
        break;
      case DIR_DOWN:
        deltaY = speed;
        deltaYCell = cellSpeed;
        break;
    }

    addPastPosition(world.getTimer());
    world.setChangedThisFrame(true);

    int nextXCell = getxCell() + deltaXCell;
    int nextYCell = getyCell() + deltaYCell;
    float nextX = getX() + deltaX;
    float nextY = getY() + deltaY;

    // If there is an enemy at the next location, restart the level
    if (world.categoryAtLocation(nextXCell, nextYCell, "character")) {
      world.reset();
      return;
    }

    // Make sure the position isn't occupied!
    if (!world.isBlocked(nextXCell, nextYCell, direction)) {
      world.moveReference(getCellPosition(), nextXCell, nextYCell);
      snapToGrid(nextX, nextY);
    }
  }
}
