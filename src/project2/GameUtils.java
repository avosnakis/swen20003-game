package project2;

import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Static class containing useful methods used in multiple other classes, chiefly related
 * to processing coordinates with regards to directions.
 *
 * @author Alex Vosnakis 743936
 */
public class GameUtils {

  public static final int CELL_SIZE = 1;

  /**
   * Determine which of the specified keys were pressed by the player and add them to an array.
   *
   * @param input The Slick Input object to check.
   * @param keys  An array of keys to check if the player pressed.
   * @return An ArrayList of key codes that the player pressed.
   */
  public static ArrayList<Integer> getPressedKeys(Input input, Integer... keys) {
    ArrayList<Integer> keysPressed = new ArrayList<>(Arrays.asList(keys));
    keysPressed.removeIf(key -> !input.isKeyPressed(key));
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
   * Finds the change in a specified direction.
   *
   * @param axis      The axis the coordinate lies on.
   * @param direction The direction the movement is happening in.
   * @param speed     The absolute value of the change being made.
   * @return The incremented coordinate.
   */
  public static float directionDelta(char axis, Direction direction, float speed) {
    // x coordinate cannot go up or down, so don't change it
    // y coordinate cannot go left or right, so don't change it
    if ((axis == 'x' && (direction == Direction.DIR_DOWN || direction == Direction.DIR_UP)) ||
        (axis == 'y' && (direction == Direction.DIR_LEFT || direction == Direction.DIR_RIGHT))) {
      return 0;
    } else if (axis == 'x' && direction == Direction.DIR_LEFT) {
      return -speed;
    } else if (axis == 'x' && direction == Direction.DIR_RIGHT) {
      return speed;
    } else if (axis == 'y' && direction == Direction.DIR_UP) {
      return -speed;
    } else if (axis == 'y' && direction == Direction.DIR_DOWN) {
      return speed;
    } else {
      // default case
      return 0;
    }
  }

  /**
   * Finds the change in a specified direction.
   *
   * @param axis      The axis the coordinate lies on.
   * @param direction The direction the movement is happening in.
   * @param speed     The absolute value of the change being made.
   * @return The incremented coordinate.
   */
  public static int directionDelta(char axis, Direction direction, int speed) {
    // x coordinate cannot go up or down, so don't change it
    // y coordinate cannot go left or right, so don't change it
    if ((axis == 'x' && (direction == Direction.DIR_DOWN || direction == Direction.DIR_UP)) ||
        (axis == 'y' && (direction == Direction.DIR_LEFT || direction == Direction.DIR_RIGHT))) {
      return 0;
    } else if (axis == 'x' && direction == Direction.DIR_LEFT) {
      return -speed;
    } else if (axis == 'x' && direction == Direction.DIR_RIGHT) {
      return speed;
    } else if (axis == 'y' && direction == Direction.DIR_UP) {
      return -speed;
    } else if (axis == 'y' && direction == Direction.DIR_DOWN) {
      return speed;
    } else {
      // default case
      return 0;
    }
  }

  /**
   * Determines whether the player pressed a key this frame.
   *
   * @param keys An array of keys the player pressed in this frame.
   * @return Whether or not the player pressed any arrow keys.
   */
  public static boolean playerMoved(ArrayList<Integer> keys) {
    return keys.size() > 0;
  }
}
