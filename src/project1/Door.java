package project1;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.Observable;
import java.util.Observer;

public class Door extends Tile implements Observer {
  private boolean isOpen;
  private Image floorImage;

  public Door(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/door.png", "door", cellPosition, windowPosition);
    setPassable(false);

    isOpen = false;
    try {
      floorImage = new Image("res/floor.png");
    } catch (SlickException e) {
      e.printStackTrace();
    }
  }

  /**
   * If the door is open, render a floor tile instead.
   *
   * @param g The Slick graphics object.
   */
  @Override
  public void render(Graphics g) {
    if (isOpen) {
      floorImage.drawCentered(getX(), getY());
    } else {
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
