package project1;

/**
 * A class that records (x, y, z) coordinates or just (x, y) coordinates. Generic so as to support both coordinates
 * in the GUI and the matrix coordinates for where the sprites are stored in the World.
 *
 * @param <T> The type of the position, intended to be either an Integer or a Float.
 */
public class Position<T extends Number> {

  public final T x;
  public final T y;
  public final T z;

  public Position(T x, T y, T z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Position(Position<T> position) {
    this.x = position.x;
    this.y = position.y;
    this.z = position.z;
  }

  @Override
  public String toString() {
    return "Position{" +
        "x=" + x +
        ", y=" + y +
        ", z=" + z +
        '}';
  }
}
