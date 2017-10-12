/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project2;

import org.newdawn.slick.Input;

import java.util.ArrayList;

public class Player extends Character implements Controllable {

  public Player(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/player_left.png", "player", cellPosition, windowPosition);
  }

  @Override
  public void update(ArrayList<Integer> keysPressed, int delta, World world) {
    handlePlayerInput(keysPressed, world);
  }

  @Override
  public void handlePlayerInput(Input input) {
  }

  @Override
  public void handlePlayerInput(ArrayList<Integer> keysPressed, World world) {
    Direction direction = determineDirection(keysPressed);
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
    float deltaX = GameUtils.directionDelta('x', direction, speed);
    float deltaY = GameUtils.directionDelta('y', direction, speed);
    int deltaXCell = GameUtils.directionDelta('x', direction, cellSpeed);
    int deltaYCell = GameUtils.directionDelta('y', direction, cellSpeed);


    addPastPosition(world.getTimer());
    world.setChangedThisFrame(true);

    Position<Integer> nextPosition = new Position<>(getxCell() + deltaXCell, getyCell() + deltaYCell);

    float nextX = getX() + deltaX;
    float nextY = getY() + deltaY;

    // If there is an enemy at the next location, restart the level
    if (world.categoryAtLocation(nextPosition, "character")) {
      world.reset();
      return;
    }

    // Make sure the position isn't occupied!
    if (!world.isBlocked(nextPosition, direction)) {
      setCellPosition(nextPosition);
      snapToGrid(nextX, nextY);
    }
  }

  /**
   * Determines the Player direction based on the input. Essentially the directional controls for the game.
   *
   * @param keysPressed The Slick input object.
   * @return The direction that the player will try to move.
   */
  public static Direction determineDirection(ArrayList<Integer> keysPressed) {
    Direction direction = Direction.DIR_NONE;

    if (keysPressed.contains(Input.KEY_LEFT)) {
      direction = Direction.DIR_LEFT;
    } else if (keysPressed.contains(Input.KEY_RIGHT)) {
      direction = Direction.DIR_RIGHT;
    } else if (keysPressed.contains(Input.KEY_UP)) {
      direction = Direction.DIR_UP;
    } else if (keysPressed.contains(Input.KEY_DOWN)) {
      direction = Direction.DIR_DOWN;
    }
    return direction;
  }
}
