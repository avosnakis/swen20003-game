/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

import org.newdawn.slick.Input;

public class Player extends Character implements Controllable {

  public Player(float x, float y, int xCell, int yCell) {
    super("res/player_left.png", x, y, xCell, yCell);
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
    int dir = DIR_NONE;

    if (input.isKeyPressed(Input.KEY_LEFT)) {
      dir = DIR_LEFT;
    } else if (input.isKeyPressed(Input.KEY_RIGHT)) {
      dir = DIR_RIGHT;
    } else if (input.isKeyPressed(Input.KEY_UP)) {
      dir = DIR_UP;
    } else if (input.isKeyPressed(Input.KEY_DOWN)) {
      dir = DIR_DOWN;
    }

    if (dir == DIR_NONE) {
      return;
    }

    // Move to our destination
    this.moveToDestination(dir, world);
  }
}
