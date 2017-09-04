package project1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import project1.tiles.Tile;
import project1.tiles.Stone;
import project1.tiles.Wall;
import project1.tiles.Floor;
import project1.tiles.Target;

import static project1.App.SCREEN_HEIGHT;
import static project1.App.SCREEN_WIDTH;


/**
 * Class containing all data for the current level of the game.
 */
public class World {

  /**
   * Array containing all sprites in the current level.
   */
  private Tile[][][] grid;

  /**
   * The player character.
   */
  private Player player;

  /**
   * Path to the level file. TODO Ask what we're supposed to do with this.
   */
  private static String levelFile = "./res/levels/0.lvl";

  /**
   * Instantiate a world populated with tiles and a player.
   */
  public World() {
    final int X_DIMENSION_INDEX = 0;
    final int Y_DIMENSION_INDEX = 1;
    final int NUM_DIMENSIONS = 2;

    // For now, assume there can be a maximum of 5 tiles at a single (x,y) coordinate.
    final int HEIGHT = 5;

    Sprite[] sprites = Loader.loadSprites(levelFile);
    int[] dimensions = new int[NUM_DIMENSIONS];
    try {
      dimensions = Loader.readMapDimensions(new BufferedReader(new FileReader(levelFile)).readLine());
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    int xDimension = dimensions[X_DIMENSION_INDEX];
    int yDimension = dimensions[Y_DIMENSION_INDEX];

    int xOffset = Loader.offset(xDimension, SCREEN_WIDTH);
    int yOffset = Loader.offset(yDimension, SCREEN_HEIGHT);

    this.grid = new Tile[xDimension][yDimension][HEIGHT];

    for (Sprite sprite : sprites) {
      this.createTile(sprite, xOffset, yOffset, xDimension, yDimension);
    }
  }

  /**
   * Passes all inputs to the player so it can be processed.
   *
   * @param input The Slick user input object.
   * @param delta Time passed since the last frame (milliseconds).
   */
  public void update(Input input, int delta) {
    this.player.update(input, this.grid);
  }

  /**
   * Draws all sprites to the screen.
   *
   * @param g The Slick graphics object.
   */
  public void render(Graphics g) {
    // Render all not-null tiles in the world
    for (Tile[][] row : this.grid) {
      for (Tile[] column : row) {
        for (Tile tile : column) {
          if (tile != null) {
            tile.render(g);
          }
        }
      }
    }
    // Render the player last
    this.player.render(g);
  }

  /**
   * Instantiates a tile of the specified type and places it at the appropriate place in the grid.
   *
   * @param sprite     The sprite for the tile being created.
   * @param xOffset    The screen offset in the x direction for the current world.
   * @param yOffset    The screen offset in the y direction for the current world.
   * @param xDimension The number of cells in the x direction for the current world.
   * @param yDimension The number of cells in the y direction for the current world.
   */
  private void createTile(Sprite sprite, float xOffset, float yOffset, int xDimension, int yDimension) {
    int xCell = sprite.getxCell();
    int yCell = sprite.getyCell();

    Tile tile = null;
    switch (sprite.getSpriteType()) {
      case "wall":
        tile = new Wall(sprite, xCell, yCell);
        break;
      case "stone":
        tile = new Stone(sprite, xCell, yCell);
        break;
      case "floor":
        tile = new Floor(sprite, xCell, yCell);
        break;
      case "target":
        tile = new Target(sprite, xCell, yCell);
        break;
      case "player":
        this.player = new Player(sprite, xCell, yCell);
        break;
      default:
        System.exit(1);
        break;
    }

    if (!sprite.getSpriteType().equals("player")) {
      this.insertTile(tile, xCell, yCell);
    }
  }

  /**
   * Inserts a tile at the specified location in the world grid.
   *
   * @param tile  The tile to be inserted.
   * @param xCell The x coordinate of the cell insert the tile to.
   * @param yCell The y coordinate of the cell insert the tile to.
   */
  private void insertTile(Tile tile, int xCell, int yCell) {
    int i = 0;
    while (this.grid[xCell][yCell][i] != null) {
      i++;
    }
    this.grid[xCell][yCell][i] = tile;
  }
}