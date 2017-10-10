package project1;

import java.util.Observable;

public class SwitchNotifier extends Observable {
  private boolean active;

  public SwitchNotifier(Door door) {
    super();
    addObserver(door);
  }

  public void update(Position<Integer> position, World world) {
    if (world.categoryAtLocation(position, "block") && !active) {
      setChanged();
      active = true;
      notifyObservers();
    } else if (active && !world.categoryAtLocation(position, "block")){
      setChanged();
      active = false;
      notifyObservers();
    }
  }

  public boolean isActive() {
    return active;
  }
}
