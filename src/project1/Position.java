package project1;

public class Position<T extends Number> {

  private T x;
  private T y;
  private T z;

  public Position(T x, T y, T z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Position(T x, T y) {
    this.x = x;
    this.y = y;
  }

  public Position(Position<T> position) {
    this.x = position.x;
    this.y = position.y;
    this.z = position.z;
  }

  public T getX() {
    return x;
  }

  public void setX(T x) {
    this.x = x;
  }

  public T getY() {
    return y;
  }

  public void setY(T y) {
    this.y = y;
  }

  public T getZ() {
    return z;
  }

  public void setZ(T z) {
    this.z = z;
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
