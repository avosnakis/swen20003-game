package project1;

/**
 * Timer class. Controls updating a counter, and checking whether a specified timeout has been surpassed.
 */
public class Timer {

  /**
   * The actual counter.
   */
  private int counter;

  /**
   * An optional value to check against to see if it has been passed.
   */
  private int timeout;

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
