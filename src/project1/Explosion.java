package project1;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Explosion {
  private static final int EXPLOSION_TIME = 400;

  private int timer;

  private Position<Float> windowPosition;
  private Image image;

  public Explosion(Position<Float> windowPosition) {
    this.windowPosition = windowPosition;

    try {
      this.image = new Image("res/explosion.png");
    } catch (SlickException e) {
      e.printStackTrace();
      System.exit(1);
    }

    this.timer = 0;
  }

  public void update(Input input, int delta, World world) {
    this.timer += delta;
  }
}
