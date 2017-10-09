package project1;

public class Timer {
  private int timeout;
  private int counter;

  public Timer(int timeout) {
    this.timeout = timeout;
    counter = 0;
  }

  public void increment(int delta) {
    counter += delta;
  }

  public boolean reachedTimeout() {
    return counter > timeout;
  }

  public void reset() {
    counter = 0;
  }
}
