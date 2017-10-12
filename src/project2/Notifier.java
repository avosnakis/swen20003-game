package project2;

import java.util.Observable;
import java.util.Observer;

/**
 * Simple class for notifying Observers.
 *
 * @param <T> Allows it to take any Observer.
 */
public class Notifier<T extends Observer> extends Observable {
  public Notifier(T t) {
    super();
    addObserver(t);
  }

  /**
   * Default alert.
   */
  public void alert() {
    setChanged();
    notifyObservers();
  }

  /**
   * Alert that sends a boolean as a payload.
   *
   * @param bool The boolean payload.
   */
  public void alert(boolean bool) {
    setChanged();
    notifyObservers(bool);
  }
}
