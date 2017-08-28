package project1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static project1.App.TILE_SIZE;
import static project1.App.SCREEN_HEIGHT;
import static project1.App.SCREEN_WIDTH;

public class Loader {

  private static final int X_DIMENSION_INDEX = 0;
  private static final int Y_DIMENSION_INDEX = 1;

  private static final String COMMA_SEPARATOR = ",";

  /**
   * Resource file paths. File paths in Java are OS-agnostic, so standard Unix file paths were used.
   * In the case of RESOURCE_PATH, the project working directory is the top level of the project, so
   * we use . to use relative paths.
   * */
  private static final String RESOURCE_PATH = "./res/";
  private static final String STONE_FILE = "stone.png";
  private static final String FLOOR_FILE = "floor.png";
  private static final String TARGET_FILE = "target.png";
  private static final String WALL_FILE = "wall.png";
  private static final String PLAYER_FILE = "player_left.png";

  /**
   * Checks if a tile is a valid position to move to, and returns true if it is.
   *
   * @param x The x-coordinate of the tile to be checked.
   * @param y The y-coordinate of the tile to be checked.
   * @return True if the tile is valid to move to, false if it isn't.
   * */
  public static boolean isBlocked(float x, float y, Sprite[] levelSprites) {
    // Handle for the player somehow attempting to move offscreen
    if (x < 0 || x > SCREEN_WIDTH || y < 0 || y > SCREEN_HEIGHT) {
      return false;
    }
    // Iterate over every sprite in the level until we find the non-player sprite that the player will next occupy,
    // and then check if it is a wall
    for (Sprite sprite : levelSprites) {
      if (sprite.pointIsInside(x, y) && !sprite.isPlayer()) {
        return sprite.isWall();
      }
    }
    // If we check every sprite, return false by default
    return false;
  }

  /**
   * Loads the sprites from a given file.
   *
   * @param filename The .lvl we are reading.
   * @return An array of all sprites in this level.
   * */
  public static Sprite[] loadSprites(String filename) {
    // Use an ArrayList to initially load data as it is dynamic
    ArrayList<Sprite> sprites = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      int[] levelDimensions = readMapDimensions(br.readLine());
      // Calculate the offset to centre the level in the middle of the window
      int xOffset = (SCREEN_WIDTH - (levelDimensions[X_DIMENSION_INDEX] * TILE_SIZE)) / 2;
      int yOffset = (SCREEN_HEIGHT - (levelDimensions[Y_DIMENSION_INDEX] * TILE_SIZE)) / 2;

      // We assume there is one sprite's information per line
      String text;
      while ((text = br.readLine()) != null) {
        sprites.add(loadSprite(text, xOffset, yOffset));
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
    return sprites.toArray(new Sprite[sprites.size()]);
  }

  /**
   * Find the dimensions of the level we are reading from the first line of the .lvl file.
   *
   * @param text The dimensions line of the .lvl file. Eg: 12,12
   * @return An array containing { xDimension, yDimension }
   * */
  private static int[] readMapDimensions(String text) {
    final int NUM_DIMENSIONS = 2;

    String[] line = text.split(COMMA_SEPARATOR);
    int[] levelDimensions = new int[NUM_DIMENSIONS];

    levelDimensions[X_DIMENSION_INDEX] = Integer.parseInt(line[X_DIMENSION_INDEX]);
    levelDimensions[Y_DIMENSION_INDEX] = Integer.parseInt(line[Y_DIMENSION_INDEX]);
    return levelDimensions;
  }

  /**
   * Load a single sprite from a line in the .lvl file.
   *
   * @param text The type, x coordinate, and y coordinate of the sprite. Eg: wall,5,5
   * @return A sprite instantiated at the specified x coordinate, y coordinate, and with the correct image.
   * */
  private static Sprite loadSprite(String text, int xOffset, int yOffset) {
    final int TYPE_INDEX = 0;
    final int X_INDEX = 1;
    final int Y_INDEX = 2;
    String filepath = RESOURCE_PATH;

    String[] line = text.split(",");
    switch (line[TYPE_INDEX]) {
      case "wall":
        filepath += WALL_FILE;
        break;
      case "floor":
        filepath += FLOOR_FILE;
        break;
      case "stone":
        filepath += STONE_FILE;
        break;
      case "target":
        filepath += TARGET_FILE;
        break;
      case "player":
        filepath += PLAYER_FILE;
        break;
      default:
        break;
    }
    return new Sprite(filepath,
        line[TYPE_INDEX],
        TILE_SIZE * Float.parseFloat(line[X_INDEX]) + xOffset,
        TILE_SIZE * Float.parseFloat(line[Y_INDEX]) + yOffset);
  }
}
