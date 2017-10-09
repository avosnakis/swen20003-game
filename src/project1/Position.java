package project1;

/**
 * A class that records (x, y) coordinates. Generic so as to support both coordinates
 * in the GUI and the grid coordinates for where the sprites are in the actual level.
 *
 * @param <T> The type of the position, intended to be either an Integer or a Float.
 */
public class Position<T extends Number> {

  public final T x;
  public final T y;

  public Position(T x, T y) {
    this.x = x;
    this.y = y;
  }

  public Position(Position<T> position) {
    this.x = position.x;
    this.y = position.y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Position<?> position = (Position<?>) o;

    if (x != null ? !x.equals(position.x) : position.x != null) return false;
    return y != null ? y.equals(position.y) : position.y == null;
  }

  @Override
  public String toString() {
    return "Position{" +
        "x=" + x +
        ", y=" + y +
        '}';
  }
}
