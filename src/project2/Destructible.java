package project2;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Interface for sprites that can be destroyed.
 *
 * @author Alex Vosnakis 743936
 */
public interface Destructible {
  public void destroy();

  public void destroy(Position<Integer> position);
}
