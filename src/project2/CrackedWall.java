package project2;

import org.newdawn.slick.Graphics;

import java.util.Observable;
import java.util.Observer;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Class for the CrackedWall tile.
 *
 * @author Alex Vosnakis 743936
 */
public class CrackedWall extends Tile implements Destructible, Observer {
  private boolean destroyed;

  public CrackedWall(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/cracked_wall.png", "cracked", cellPosition, windowPosition);
    setPassable(false);

    destroyed = false;
  }

  /**
   * Only render it if it is not destroyed.
   *
   * @param g The Slick graphics object.
   */
  @Override
  public void render(Graphics g) {
    if (!destroyed) {
      super.render(g);
    }
  }

  /**
   * When notified by the corresponding Tnt's Notifier, destroy this CrackedWall.
   *
   * @param o   The Notifier.
   * @param arg Null object (ignored)
   */
  @Override
  public void update(Observable o, Object arg) {
    destroy();
  }

  /**
   * Set this CrackedWall to destroyed, and make it passable.
   */
  public void destroy() {
    setPassable(true);
    destroyed = true;
  }

  public void destroy(Position<Integer> position) {
  }
}
