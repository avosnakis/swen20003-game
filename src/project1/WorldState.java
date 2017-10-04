package project1;

import java.util.ArrayList;
import java.util.Arrays;

public class WorldState {
  public static final int NO_INDEX = -1;
  public static final int LENGTH = 5;

  private int[][][] spriteIndices;

  public WorldState(ArrayList<Sprite> sprites) {
    // Initialise the spriteIndices array to holding null values.
    spriteIndices = new int[Loader.getWorldWidth()][Loader.getWorldHeight()][LENGTH];
    for (int[][] plane : spriteIndices) {
      for (int[] row : plane) {
        Arrays.fill(row, NO_INDEX);
      }
    }

    // Populate the spriteIndices array with the actual values.
    for (int i = 0; i < sprites.size(); i++) {
      Sprite sprite = sprites.get(i);
      spriteIndices[sprite.getxCell()][sprite.getyCell()][sprite.getzCell()] = i;
    }
  }

  public WorldState(WorldState worldState) {
    // Copy the data from another worldState.
    spriteIndices = new int[Loader.getWorldWidth()][Loader.getWorldHeight()][LENGTH];
    for (int i = 0; i < Loader.getWorldWidth(); i++) {
      for (int j = 0; j < Loader.getWorldHeight(); j++) {
        System.arraycopy(worldState.getArrayAt(i, j), 0, spriteIndices[i][j], 0, LENGTH);
      }
    }
  }

  /**
   * Remove the value from a position, and shift the array at the (x, y) to ensure that all the filled positions are
   * at the bottom.
   *
   * @param position The position to remove the value from.
   */
  public void removeValue(Position<Integer> position) {
    spriteIndices[position.x][position.y][position.z] = NO_INDEX;
    shiftDown(position.x, position.y);
  }

  /**
   * Set the value at the specified position to the specified value.
   *
   * @param position The position with the value to be set.
   * @param value    The value to set at the position.
   */
  public void setValue(Position<Integer> position, int value) {
    spriteIndices[position.x][position.y][position.z] = value;
  }

  /**
   * Returns the array at an (x, y) coordinate.
   *
   * @param x The x coordinate of the array.
   * @param y The y coordinate of the array.
   * @return The array at the (x, y) coordinate.
   */
  public int[] getArrayAt(int x, int y) {
    return spriteIndices[x][y];
  }

  /**
   * Returns the value at a position.
   *
   * @param position The (x, y, z) position.
   * @return The value at the position.
   */
  public int getValueAt(Position<Integer> position) {
    return spriteIndices[position.x][position.y][position.z];
  }

  /**
   * Returns the value at an (x, y, z) coordinate.
   *
   * @param x The x coordinate of the position.
   * @param y The y coordinate of the position.
   * @param z The z coordinate of the position.
   * @return The value at the position.
   */
  public int getValueAt(int x, int y, int z) {
    return spriteIndices[x][y][z];
  }

  /**
   * Finds the first empty z coordinate at an (x, y) coordinate.
   *
   * @param x The x coordinate of the position to check.
   * @param y The y coordinate of the position to check.
   * @return The first empty z coordinate.
   */
  public int findEmptyZ(int x, int y) {
    int z = 0;
    while (spriteIndices[x][y][z] != NO_INDEX) {
      z++;
    }
    return z;
  }

  /**
   * Moves a sprite index from one (x, y, z) coordinate to another.
   *
   * @param initialPosition The initial (x, y, z) coordinate of the sprite.
   * @param finalPosition   The final (x, y, z) coordinate of the sprite.
   */
  public void moveIndex(Position<Integer> initialPosition, Position<Integer> finalPosition) {
    int spriteIndex = spriteIndices[initialPosition.x][initialPosition.y][initialPosition.z];

    removeValue(initialPosition);
    setValue(finalPosition, spriteIndex);
  }

  /**
   * Shifts the z-axis at a specified coordinate, so that all empty slots are at the top of the matrix.
   *
   * @param x The x-coordinate of the z-axis to shift.
   * @param y The y-coordinate of the z-axis to shift.
   */
  private void shiftDown(int x, int y) {
    for (int i = 0; i < spriteIndices[x][y].length - 1; i++) {
      if (spriteIndices[x][y][i] == NO_INDEX) {
        spriteIndices[x][y][i] = spriteIndices[x][y][i + 1];
        spriteIndices[x][y][i + 1] = NO_INDEX;
      }
    }
  }

  @Override
  public String toString() {
    ArrayList<String> arrayStrings = new ArrayList<>();
    for (int i = 0; i < spriteIndices.length; i++) {
      for (int j = 0; j < spriteIndices.length; j++) {
        arrayStrings.add(Arrays.toString(spriteIndices[i][j]));
      }
    }
    return arrayStrings.toString();
  }
}
