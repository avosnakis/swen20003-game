package project1;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class World {

  private Sprite[] sprites;

  private static String levelFile = "./res/levels/0.lvl";

  public World() {
    this.sprites = Loader.loadSprites(levelFile);
  }

  public void update(Input input, int delta) {
  }

  public void render(Graphics g) {
    for (Sprite sprite : this.sprites) {
      sprite.render(g);
    }
  }
}
