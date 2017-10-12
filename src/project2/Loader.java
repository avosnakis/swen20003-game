package project2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * SWEN20003 Assignment 2B
 *
 * Template taken from:
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 *
 * Static class for loading the level files.
 *
 * @author Alex Vosnakis 743936
 */
public class Loader {

  private static int worldWidth;
  private static int worldHeight;
  private static int offsetX;
  private static int offsetY;

  /**
   * Create the appropriate sprite given a name and location.
   *
   * @param name the name of the sprite
   * @param x    the x position
   * @param y    the y position
   * @return the sprite object
   */
  private static Sprite createSprite(String name, float x, float y, int xCell, int yCell) {
    Position<Integer> cellPosition = new Position<>(xCell, yCell);
    Position<Float> windowPosition = new Position<>(x, y);

    switch (name) {
      case "wall":
        return new Wall(cellPosition, windowPosition);
      case "floor":
        return new Floor(cellPosition, windowPosition);
      case "target":
        return new Target(cellPosition, windowPosition);
      case "switch":
        return new Switch(cellPosition, windowPosition);
      case "door":
        return new Door(cellPosition, windowPosition);
      case "cracked":
        return new CrackedWall(cellPosition, windowPosition);
      case "stone":
        return new Stone(cellPosition, windowPosition);
      case "ice":
        return new Ice(cellPosition, windowPosition);
      case "tnt":
        return new Tnt(cellPosition, windowPosition);
      case "mage":
        return new Mage(cellPosition, windowPosition);
      case "skeleton":
        return new Skeleton(cellPosition, windowPosition);
      case "rogue":
        return new Rogue(cellPosition, windowPosition);
      case "player":
        return new Player(cellPosition, windowPosition);
    }
    return null;
  }


  /**
   * Loads the sprites from a given file.
   *
   * @param filename The file to load the level from.
   * @return A collection of all sprites in this level.
   */
  public static ArrayList<Sprite> loadSprites(String filename) {
    ArrayList<Sprite> list = new ArrayList<>();

    // Open the file
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;

      // Find the world size
      line = reader.readLine();
      String[] parts = line.split(",");
      worldWidth = Integer.parseInt(parts[0]);
      worldHeight = Integer.parseInt(parts[1]);

      // Calculate the top left of the tiles so that the level is
      // centred
      offsetX = (App.SCREEN_WIDTH - worldWidth * App.TILE_SIZE) / 2;
      offsetY = (App.SCREEN_HEIGHT - worldHeight * App.TILE_SIZE) / 2;

      // Loop over every line of the file
      while ((line = reader.readLine()) != null) {
        String name;

        // Split the line into parts
        parts = line.split(",");
        name = parts[0];
        int xCell = Integer.parseInt(parts[1]);
        int yCell = Integer.parseInt(parts[2]);

        // Adjust for the grid
        float x = cellToWindow(xCell, 'x');
        float y = cellToWindow(yCell, 'y');

        // Create the sprite
        list.add(createSprite(name, x, y, xCell, yCell));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return list;
  }

  /**
   * Converts cell coordinate to window coordinate.
   *
   * @param cell The cell coordinate to transform.
   * @param axis The axis this cell coordinate lies on.
   * @return The corresponding window coordinate for this cell.
   */
  public static float cellToWindow(int cell, char axis) {
    if (axis == 'x') {
      return App.TILE_SIZE * cell + offsetX;
    } else {
      return App.TILE_SIZE * cell + offsetY;
    }
  }

  public static int getWorldWidth() {
    return worldWidth;
  }

  public static int getWorldHeight() {
    return worldHeight;
  }

  public static int getOffsetX() {
    return offsetX;
  }

  public static int getOffsetY() {
    return offsetY;
  }
}
