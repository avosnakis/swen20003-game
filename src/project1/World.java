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

  /** Passes all inputs to the player so it can be processed. */
  public void update(Input input, int delta) {
    this.player.update(input);
  }

  /** Draws all sprites to the screen. */
  public void render(Graphics g) {
    for (Sprite sprite : this.sprites) {
      sprite.render(g);
    }
  }

  /** Returns the index of the player sprite.
   *
   * @param sprites An array of all sprites.
   * @return i The index of the player sprite, or -1 if it wasn't found. */
  private static int indexOfPlayerSprite(Sprite[] sprites) {
    for (int i = 0; i < sprites.length; i++) {
      if (sprites[i].getSpriteType().equals("player")) {
        return i;
      }
    }
    return -1;
  }
}