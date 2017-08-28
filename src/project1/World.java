package project1;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * Class containing all data for the current level of the game.
 */
public class World {

  /**
   * Array containing all sprites in the current level.
   */
  private Sprite[] sprites;

  /**
   * The player character.
   */
  private Player player;

  private static String levelFile = "./res/levels/0.lvl";

  /**
   * Error code for no player sprite in the level.
   */
  private static final int NO_PLAYER_SPRITE = -1;

  public World() {
    this.sprites = Loader.loadSprites(levelFile);

    int playerSpriteIndex = indexOfPlayerSprite(this.sprites);
    if (playerSpriteIndex == NO_PLAYER_SPRITE) {
      // For now we assume there must always be a player in the level, so we exit if there is not.
      System.exit(1);
    }
    this.player = new Player(this.sprites[playerSpriteIndex]);
  }

  /**
   * Passes all inputs to the player so it can be processed.
   *
   * @param input The Slick user input object.
   * @param delta Time passed since the last frame (milliseconds).
   */
  public void update(Input input, int delta) {
    this.player.update(input, this.sprites);
  }

  /**
   * Draws all sprites to the screen.
   *
   * @param g The Slick graphics object.
   */
  public void render(Graphics g) {
    for (Sprite sprite : this.sprites) {
      sprite.render(g);
    }
  }

  /**
   * Returns the index of the player sprite.
   *
   * @param sprites An array of all sprites.
   * @return The index of the player sprite, or NO_PLAYER_SPRITE if it wasn't found.
   */
  private static int indexOfPlayerSprite(Sprite[] sprites) {
    for (int i = 0; i < sprites.length; i++) {
      if (sprites[i].isPlayer()) {
        return i;
      }
    }
    return NO_PLAYER_SPRITE;
  }
}