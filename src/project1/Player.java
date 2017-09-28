/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

import org.newdawn.slick.Input;

public class Player extends Character implements Controllable {

  public Player(float x, float y, int xCell, int yCell) {
    super("res/player_left.png", "player", x, y, xCell, yCell);
  }

  @Override
  public void update(Input input, int delta, World world) {
    this.handlePlayerInput(input, world);
  }

  @Override
  public void handlePlayerInput(Input input) {
  }

  @Override
  public void handlePlayerInput(Input input, World world) {
    Direction direction = Direction.DIR_NONE;

    if (input.isKeyPressed(Input.KEY_LEFT)) {
      direction = Direction.DIR_LEFT;
    } else if (input.isKeyPressed(Input.KEY_RIGHT)) {
      direction = Direction.DIR_RIGHT;
    } else if (input.isKeyPressed(Input.KEY_UP)) {
      direction = Direction.DIR_UP;
    } else if (input.isKeyPressed(Input.KEY_DOWN)) {
      direction = Direction.DIR_DOWN;
    }

    if (direction == Direction.DIR_NONE) {
      return;
    }

    world.incrementMoves();
    // Move to our destination
    this.moveToDestination(direction, world);
  }
}
