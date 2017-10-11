package project1;

import java.util.Observable;

/**
 * Class that notifies the corresponding Door whenever the switch is activated or deactivated.
 */
public class SwitchNotifier extends Observable {
  private boolean active;

  public SwitchNotifier(Door door) {
    super();
    addObserver(door);
  }

  /**
   * If the switch is inactive and a block is on top of it, activate it and notify observers.
   * Otherwise if the switch is active and a block is not on top of it, deactivate it and notify observers.
   *
   * @param position The position to check whether there is a block.
   * @param world The World the Switch is in.
   */
  public void update(Position<Integer> position, World world) {
    if (world.categoryAtLocation(position, "block") && !active) {
      active = true;
      setChanged();
      notifyObservers();
    } else if (active && !world.categoryAtLocation(position, "block")) {
      active = false;
      setChanged();
      notifyObservers();
    }
  }

  public boolean isActive() {
    return active;
  }
}
