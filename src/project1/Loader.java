package project1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Loader {

  /* Every entity has a width and length of 32 pixels*/
  private static final int ENTITY_WIDTH = 32;

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
	public static ArrayList<Sprite> loadSprites(String filename) {
	  ArrayList<Sprite> sprites = new ArrayList<Sprite>();

	  try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String text = br.readLine();
      while ((text = br.readLine()) != null) {

      }
    } catch (IOException e) {
	    e.printStackTrace();
    }
		return null;
	}

	private static int[] readMapDimensions(String text) {
	  return null;
  }
}
