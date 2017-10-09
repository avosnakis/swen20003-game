package project1;

public class Timer {
  private int timeout;
  private int counter;

  public Timer(int timeout) {
    this.timeout = timeout;
    counter = 0;
  }

  /**
   * If a timeout is not needed, then we give a timeout of -1.
   */
  public Timer() {
    timeout = -1;
    counter = 0;
  }

  public void increment(int delta) {
    counter += delta;
  }

  public boolean reachedTimeout() {
    return counter >= timeout;
  }

  public void reset() {
    counter = 0;
  }

  public int getCounter() {
    return counter;
  }
}
