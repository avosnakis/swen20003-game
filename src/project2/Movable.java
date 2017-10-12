package project2;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Interface for all classes that can change their position. Gives them movement and undo capability.
 *
 * @author Alex Vosnakis 743936
 */
public interface Movable {
  public void moveToDestination(Direction direction, World world);

  public void undo(int time);

  public void addPastPosition(int time);
}
