package project1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static project1.App.TILE_SIZE;

public class Loader {

  private static final int NUM_DIMENSIONS = 2;
  private static final int X_INDEX = 0;
  private static final int Y_INDEX = 1;

  private static final char COMMA_SEPARATOR = ',';

	// Converts a world coordinate to a tile coordinate,
	// and returns if that location is a blocked tile
	public static boolean isBlocked(float x, float y) {
		// Default to blocked
		return true;
	}
		
	/**
	 * Loads the sprites from a given file.
	 * @param filename
	 * @return
	 */
	public static Sprite[] loadSprites(String filename) {

	  try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      int[] levelDimensions = readMapDimensions(br.readLine());
      int xDimension = levelDimensions[X_INDEX];
      int yDimension = levelDimensions[Y_INDEX];

      String text;
      while ((text = br.readLine()) != null) {

      }
    } catch (IOException e) {
	    e.printStackTrace();
    }
    return null;
	}

	/* Find the dimensions of the level we are reading from the first line of the .lvl file*/
	private static int[] readMapDimensions(String text) {
	  int[] levelDimensions = new int[NUM_DIMENSIONS];
    int separatorPosition = text.indexOf(COMMA_SEPARATOR);

    /* X dimension is the first number */
    levelDimensions[X_INDEX] = Integer.parseInt(text.substring(0, COMMA_SEPARATOR));
    /* Y dimension is the second number. We add 1 to end index as it is exclusive */
    levelDimensions[Y_INDEX] = Integer.parseInt(text.substring(COMMA_SEPARATOR, text.length() + 1));
	  return levelDimensions;
  }
}
