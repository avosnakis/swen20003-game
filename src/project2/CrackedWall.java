package project2;

import org.newdawn.slick.Graphics;

import java.util.Observable;
import java.util.Observer;

public class CrackedWall extends Tile implements Destructible, Observer {
  private boolean destroyed;

  public CrackedWall(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/cracked_wall.png", "cracked", cellPosition, windowPosition);
    setPassable(false);

    destroyed = false;
  }

  @Override
  public void render(Graphics g) {
    if (!destroyed) {
      super.render(g);
    }
  }

  @Override
  public void update(Observable o, Object arg) {
    destroy();
  }

  public void destroy() {
    setPassable(true);
    destroyed = true;
  }

  public void destroy(Position<Integer> position) {
  }
}
