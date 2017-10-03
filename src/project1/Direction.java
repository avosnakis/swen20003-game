package project1;

import org.newdawn.slick.Input;

/**
 * The directions that a sprite can move in.
 */
public enum Direction {
  DIR_NONE,
  DIR_LEFT,
  DIR_RIGHT,
  DIR_UP,
  DIR_DOWN;

  /**
   * Determines the direction based on the input. Essentially the direcitonal controls for the game.
   *
   * @param input The Slick input object.
   * @return The direction that the player will try to move.
   */
  public static Direction determineDirection(Input input) {
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
    return direction;
  }
}
