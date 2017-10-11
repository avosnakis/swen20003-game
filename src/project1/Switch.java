package project1;

import java.util.ArrayList;

public class Switch extends Tile {

  private SwitchNotifier notifier;

  public Switch(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/switch.png", "switch", cellPosition, windowPosition);
    setPassable(true);

    notifier = null;
  }

  @Override
  public void update(ArrayList<Integer> keysPressed, int delta, World world) {
    // On the first frame, add the door as the observer
    if (notifier == null) {
      notifier = new SwitchNotifier(world.findDoor());
    }

    notifier.update(getCellPosition(), world);
  }
}
