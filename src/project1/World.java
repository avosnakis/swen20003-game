package project1;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class World {

  private Sprite[] sprites;
  private Player player;

  private static String levelFile = "./res/levels/0.lvl";

  public World() {
    this.sprites = Loader.loadSprites(levelFile);

    int playerSpriteIndex = indexOfPlayerSprite(this.sprites);
    if (playerSpriteIndex == -1) {
      System.exit(-1);
    }
    this.player = new Player(this.sprites[playerSpriteIndex]);
  }

  public void update(Input input, int delta) {
  }

  public void render(Graphics g) {
    for (Sprite sprite : this.sprites) {
      sprite.render(g);
    }
  }

  private static int indexOfPlayerSprite(Sprite[] sprites) {
    for (int i = 0; i < sprites.length; i++) {
      if (sprites[i].getSpriteType().equals("player")) {
        return i;
      }
    }
    return -1;
  }
}