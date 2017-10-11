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

  public void update(Position<Integer> position, World world) {
    if (world.categoryAtLocation(position, "block") && !active) {
      active = true;
      setChanged();
      notifyObservers();
    } else if (active && !world.categoryAtLocation(position, "block")){
      active = false;
      setChanged();
      notifyObservers();
    }
  }

  public boolean isActive() {
    return active;
  }
}
