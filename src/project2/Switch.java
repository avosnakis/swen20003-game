package project2;

import java.util.ArrayList;

public class Switch extends Tile {

  private boolean active;
  private Notifier<Door> notifier;

  public Switch(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/switch.png", "switch", cellPosition, windowPosition);
    setPassable(true);

    notifier = null;
    active = false;
  }

  @Override
  public void update(ArrayList<Integer> keysPressed, int delta, World world) {
    // On the first frame, add the door as the observer
    if (notifier == null) {
      notifier = new Notifier<>((Door) world.findUniqueSprite("door"));
    }

    if (world.categoryAtLocation(getCellPosition(), "block") && !active) {
      notifier.alert(true);
      active = true;
    } else if (active && !world.categoryAtLocation(getCellPosition(), "block")) {
      notifier.alert(false);
      active = false;
    }
  }
}
