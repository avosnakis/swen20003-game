package project2;

import java.util.Observable;
import java.util.Observer;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Simple class for notifying Observers.
 *
 * @param <T> Allows it to take any Observer.
 * @author Alex Vosnakis 743936
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
