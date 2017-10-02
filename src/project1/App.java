/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Input;

/**
 * Main class for the game.
 * Handles initialisation, input and rendering.
 */
public class App extends BasicGame {
  /**
   * screen width, in pixels
   */
  public static final int SCREEN_WIDTH = 800;
  /**
   * screen height, in pixels
   */
  public static final int SCREEN_HEIGHT = 600;
  /**
   * size of the tiles, in pixels
   */
  public static final int TILE_SIZE = 32;

  private static final String[] levels = {
      "0.lvl",
      "1.lvl",
      "2.lvl",
      "3.lvl",
      "4.lvl",
      "5.lvl",
      "6.lvl"
  };

  private int currentLevel;
  private World world;

  public App() {
    super("Shadow Blocks");
    this.currentLevel = 1;
  }

  @Override
  public void init(GameContainer gc) throws SlickException {
    this.world = new World("res/levels/" + levels[this.currentLevel]);
  }

  /**
   * Update the game state for a frame.
   *
   * @param gc    The Slick game container object.
   * @param delta Time passed since last frame (milliseconds).
   */
  @Override
  public void update(GameContainer gc, int delta) throws SlickException {
    // If the player completed the level on the previous frame,
    // move to the next level and skip the rest of this frame
    if (this.world.hasWon()) {
      this.currentLevel += 1;
      this.world = new World("res/levels/" + levels[this.currentLevel]);
      return;
    }

    // Get data about the current input (keyboard state).
    Input input = gc.getInput();
    this.world.update(input, delta);
  }

  /**
   * Render the entire screen, so it reflects the current game state.
   *
   * @param gc The Slick game container object.
   * @param g  The Slick graphics object, used for drawing.
   */
  public void render(GameContainer gc, Graphics g) throws SlickException {
    this.world.render(g);
  }

  /**
   * Start-up method. Creates the game and runs it.
   *
   * @param args Command-line arguments (ignored).
   */
  public static void main(String[] args) throws SlickException {
    AppGameContainer app = new AppGameContainer(new App());
    // setShowFPS(true), to show frames-per-second.
    app.setShowFPS(false);
    app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
    app.start();
  }
}