package project1;

import org.newdawn.slick.Graphics;

import java.util.Observable;
import java.util.Observer;

public class Door extends Tile implements Observer {
  private boolean isOpen;

  public Door(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/door.png", "door", cellPosition, windowPosition);
    setPassable(false);

    isOpen = false;
  }

  /**
   * If the door is open, render a floor tile instead.
   *
   * @param g The Slick graphics object.
   */
  @Override
  public void render(Graphics g) {
    if (!isOpen) {
      super.render(g);
    }
  }

  /**
   * When the switch's state changes, change the state of the Door to match it.
   *
   * @param o The SwitchNotifier carrying the change information.
   * @param arg Null object. Ignored.
   */
  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof SwitchNotifier) {
      isOpen = ((SwitchNotifier)o).isActive();
      setPassable(isOpen);
    }
  }
}
