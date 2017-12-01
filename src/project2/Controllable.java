package project2;

import java.util.ArrayList;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Interface for all classes that react to the player's input.
 *
 * @author Alex Vosnakis 743936
 */
public interface Controllable {
  public void handlePlayerInput(ArrayList<Integer> input);

  public void handlePlayerInput(ArrayList<Integer> keysPressed, World world);

}
