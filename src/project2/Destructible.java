package project2;

/**
 * Interface for sprites that can be destroyed.
 */
public interface Destructible {
  public void destroy();

  public void destroy(Position<Integer> position);
}
