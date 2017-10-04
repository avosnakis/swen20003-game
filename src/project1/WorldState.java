package project1;

import java.util.ArrayList;
import java.util.Arrays;

public class WorldState {
  private static final int NO_INDEX = -1;

  private int[][][] spriteIndices;

  public WorldState(ArrayList<Sprite> sprites) {
    this.spriteIndices = new int[Loader.getWorldWidth()][Loader.getWorldHeight()][5];
    for (int[][] plane : this.spriteIndices) {
      for (int[] row : plane) {
        Arrays.fill(row, NO_INDEX);
      }
    }
    for (int i = 0; i < sprites.size(); i++) {
      Sprite sprite = sprites.get(i);
      this.spriteIndices[sprite.getxCell()][sprite.getyCell()][sprite.getzCell()] = i;
    }

  }

  public WorldState(WorldState worldState) {
    this.spriteIndices = new int[Loader.getWorldWidth()][Loader.getWorldHeight()][5];
    for (int i = 0; i < Loader.getWorldWidth(); i++) {
      for (int j = 0; j < Loader.getWorldHeight(); j++) {

        System.arraycopy(worldState.getArrayAt(i, j),
            0,
            spriteIndices[i][j],
            0,
            spriteIndices[i][j].length);
      }
    }
  }

  public void removeValue(int x, int y, int z) {
    this.spriteIndices[x][y][z] = NO_INDEX;
    this.shiftDown(x, y);
  }

  public void removeValue(Position<Integer> position) {
    this.spriteIndices[position.x][position.y][position.z] = NO_INDEX;
    this.shiftDown(position.x, position.y);
  }

  public void setValue(int x, int y, int z, int value) {
    this.spriteIndices[x][y][z] = value;
  }

  public int[] getArrayAt(int x, int y) {
    return this.spriteIndices[x][y];
  }

  public int getValueAt(int x, int y, int z) {
    return this.spriteIndices[x][y][z];
  }

  public int findEmptyZ(int x, int y) {
    int z = 0;
    while (this.spriteIndices[x][y][z] != NO_INDEX) {
      z++;
    }
    return z;
  }

  /**
   * Moves a sprite index from one (x, y) coordinate to another.
   *
   * @param fromX The initial x coordinate.
   * @param fromY The initial y coordinate.
   * @param fromZ The initial z coordinate.
   * @param toX   The final x coordinate.
   * @param toY   The final y coordinate.
   */
  public void moveIndex(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
    // Remove its index
    int spriteIndex = this.spriteIndices[fromX][fromY][fromZ];
    this.spriteIndices[fromX][fromY][fromZ] = NO_INDEX;
    this.shiftDown(fromX, fromY);

    // Place it in its new location
    this.spriteIndices[toX][toY][toZ] = spriteIndex;
  }

  /**
   * Shifts the z-axis at a specified coordinate, so that all empty slots are at the top of the matrix.
   *
   * @param x The x-coordinate of the z-axis to shift.
   * @param y The y-coordinate of the z-axis to shift.
   */
  private void shiftDown(int x, int y) {
    for (int i = 0; i < this.spriteIndices[x][y].length - 1; i++) {
      if (this.spriteIndices[x][y][i] == NO_INDEX) {
        this.spriteIndices[x][y][i] = this.spriteIndices[x][y][i + 1];
        this.spriteIndices[x][y][i + 1] = NO_INDEX;
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
