package project2;

import java.util.ArrayList;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Class for the Switch tile. When activated or deactived, it notifies a Door to change its state.
 *
 * @author Alex Vosnakis 743936
 */
public class Switch extends Tile {

  private boolean active;
  private Notifier<Door> notifier;

  public Switch(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/switch.png", "switch", cellPosition, windowPosition);
    setPassable(true);

    notifier = null;
    active = false;
  }

  /**
   * Update this sprite.
   *
   * @param keys  The keys the player has pressed.
   * @param delta The time since the last frame (ignored).
   * @param world The world this sprite is in.
   */
  @Override
  public void update(ArrayList<Integer> keys, int delta, World world) {
    // On the first frame, add the door as the observer
    if (notifier == null) {
      notifier = new Notifier<>((Door) world.findUniqueSprite("door"));
    }

    // If the state of the switch changes, notify the door
    if (world.spriteAtLocation(getCellPosition(), "block", Sprite.isOfCategory()) && !active) {
      notifier.alert(true);
      active = true;
    } else if (active && !world.spriteAtLocation(getCellPosition(), "block", Sprite.isOfCategory())) {
      notifier.alert(false);
      active = false;
    }
  }
}
