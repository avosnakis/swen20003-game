/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Loader {
  private static String[][] types;

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
    switch (name) {
      case "wall":
        return new Wall(x, y, xCell, yCell);
      case "floor":
        return new Floor(x, y, xCell, yCell);
      case "target":
        return new Target(x, y, xCell, yCell);
      case "switch":
        return new Switch(x, y, xCell, yCell);
      case "door":
        return new Door(x ,y, xCell, yCell);
      case "cracked":
        return new CrackedWall(x, y, xCell, yCell);
      case "stone":
        return new Stone(x, y, xCell, yCell);
      case "ice":
        return new Ice(x, y, xCell, yCell);
      case "tnt":
        return new Tnt(x, y, xCell, yCell);
      case "mage":
        return new Mage(x, y, xCell, yCell);
      case "skeleton":
        return new Skeleton(x, y, xCell, yCell);
      case "rogue":
        return new Rogue(x, y, xCell, yCell);
      case "player":
        return new Player(x, y, xCell, yCell);
    }
    return null;
  }


  /**
   * Loads the sprites from a given file.
   *
   * @param filename
   * @return
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

      // Create the array of object types
      types = new String[worldWidth][worldHeight];

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
        float x = offsetX + xCell * App.TILE_SIZE;
        float y = offsetY + yCell * App.TILE_SIZE;

        // Create the sprite
        list.add(createSprite(name, x, y, xCell, yCell));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return list;
  }

  public static int getWorldWidth() {
    return worldWidth;
  }

  public static int getWorldHeight() {
    return worldHeight;
  }
}
