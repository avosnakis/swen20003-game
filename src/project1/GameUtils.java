package project1;

import org.newdawn.slick.Input;

import java.util.ArrayList;

public class GameUtils {

  /**
   * Determine which arrow keys were pressed by the player and add them to an array.
   *
   * @param input The Slick Input object to check.
   * @retur An ArrayList of key codes that the player pressed.
   */
  public static ArrayList<Integer> recordArrowKeysPressed(Input input) {
    ArrayList<Integer> keysPressed = new ArrayList<>();
    if (input.isKeyPressed(Input.KEY_RIGHT)) {
      keysPressed.add(Input.KEY_RIGHT);
    }

    if (input.isKeyPressed(Input.KEY_UP)) {
      keysPressed.add(Input.KEY_UP);
    }

    if (input.isKeyPressed(Input.KEY_DOWN)) {
      keysPressed.add(Input.KEY_DOWN);
    }

    if (input.isKeyPressed(Input.KEY_LEFT)) {
      keysPressed.add(Input.KEY_LEFT);
    }
    return keysPressed;
  }

  /**
   * Increments a cell coordinate in the specified direction.
   *
   * @param coordinate The coordinate to be incremented.
   * @param axis       The axis the coordinate lies on.
   * @param direction  The direction the movement is happening in.
   * @return The incremented coordinate.
   */
  public static int incrementCoordinate(int coordinate, char axis, Direction direction) {
    // x coordinate cannot go up or down, so don't change it
    // y coordinate cannot go left or right, so don't change it
    if ((axis == 'x' && (direction == Direction.DIR_DOWN || direction == Direction.DIR_UP)) ||
        (axis == 'y' && (direction == Direction.DIR_LEFT || direction == Direction.DIR_RIGHT))) {
      return coordinate;
    } else if (axis == 'x' && direction == Direction.DIR_LEFT) {
      return --coordinate;
    } else if (axis == 'x' && direction == Direction.DIR_RIGHT) {
      return ++coordinate;
    } else if (axis == 'y' && direction == Direction.DIR_UP) {
      return --coordinate;
    } else if (axis == 'y' && direction == Direction.DIR_DOWN) {
      return ++coordinate;
    } else {
      // default case
      return coordinate;
    }
  }

  /**
   * Determines whether the player attempted to move in this frame.
   *
   * @param keysPressed An array of all arrow keys the player pressed in this frame.
   * @return Whether or not the player pressed any arrow keys.
   */
  public static boolean playerMoved(ArrayList<Integer> keysPressed) {
    return keysPressed.size() > 0;
  }
}
