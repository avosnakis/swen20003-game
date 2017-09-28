/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class World implements Controllable {
  private static final int NO_INDEX = -1;

  private ArrayList<Sprite> sprites;
  private ArrayList<int[]> targetLocations;
  private int[][][] spriteIndices;

  private int timer;
  private Stack<Integer> updateTimes;
  private HashMap<Integer, int[][][]> pastWorldLocations;
  private boolean updatedThisFrame;
  private int moveCount;

  public World(String filename) {
    this.sprites = Loader.loadSprites(filename);
    this.targetLocations = new ArrayList<>();

    this.spriteIndices = new int[Loader.getWorldWidth()][Loader.getWorldHeight()][5];
    for (int[][] plane : this.spriteIndices) {
      for (int[] row : plane) {
        Arrays.fill(row, NO_INDEX);
      }
    }

    for (int i = 0; i < this.sprites.size(); i++) {
      if (this.sprites.get(i).getSpriteType().equals("target")) {
        this.addTargetLocation(this.sprites.get(i));
      }
      this.insertIndex(i, this.sprites.get(i).getxCell(), this.sprites.get(i).getyCell());
    }

    this.updatedThisFrame = false;
    this.updateTimes = new Stack<>();
    this.updateTimes.push(0);
    this.pastWorldLocations = new HashMap<>();
    this.pastWorldLocations.put(0, this.spriteIndices);

    this.timer = 0;
    this.moveCount = 0;
  }

  public void update(Input input, int delta) {
    this.handlePlayerInput(input);
    this.timer += delta;
    for (Sprite sprite : this.sprites) {
      if (sprite != null) {
        sprite.update(input, delta, this);
      }
    }

    if (this.updatedThisFrame) {
      this.updateTimes.push(this.timer);
      this.pastWorldLocations.put(this.timer, this.spriteIndices);
      this.updatedThisFrame = false;
    }
  }

  public void render(Graphics g) {
    for (Sprite sprite : this.sprites) {
      if (sprite != null) {
        sprite.render(g);
      }
    }
    g.drawString(String.format("Moves: %d", this.moveCount), 0, 0);
  }

  /**
   * @return Whether all targets have been covered by a block.
   */
  public boolean hasWon() {
    boolean hasWon = false;
    for (int[] location : this.targetLocations) {
      hasWon = this.isCovered(location[0], location[1]);
      if (!hasWon) {
        return false;
      }
    }
    return hasWon;
  }

  /**
   * Checks to see whether a target at an (x,y) position has been covered by a block.
   *
   * @param x The x coordinate to check.
   * @param y The y coordinate to check.
   * @return Whether the target has been covered by a block.
   */
  private boolean isCovered(int x, int y) {
    int i = 0;
    while (this.spriteIndices[x][y][i] != NO_INDEX) {
      if (this.sprites.get(this.spriteIndices[x][y][i]).getSpriteCategory().equals("block")) {
        return true;
      }
      i++;
    }
    return false;
  }

  public void incrementMoves() {
    this.moveCount++;
  }

  /**
   * Determines whether an (x,y) position is possible to move to.
   *
   * @param x The x point to be checked.
   * @param y The y point to be checked.
   * @param direction The direction the sprite is currently moving.
   * @return Whether the position is blocked or not.
   */
  public boolean isBlocked(int x, int y, Direction direction) {
    boolean cannotMove = true;
    for (int i = 0; i < this.spriteIndices[x][y].length && this.spriteIndices[x][y][i] != NO_INDEX; i++) {
      int index = this.spriteIndices[x][y][i];
      switch (this.sprites.get(index).getSpriteCategory()) {
        case "character":
          break;
        case "tile":
          cannotMove = !this.sprites.get(index).isPassable();
          break;
        case "block":
          int nextX = incrementCoordinate(x, 'x', direction);
          int nextY = incrementCoordinate(y, 'y', direction);
          cannotMove = isBlocked(nextX, nextY, direction);
          this.sprites.get(index).moveToDestination(direction, this);
          break;
      }
      if (cannotMove) {
        return true;
      }
    }
    return false;
  }

  /**
   * Inserts a sprite index at an (x,y) coordinate.
   *
   * @param index
   * @param x
   * @param y
   */
  private void insertIndex(int index, int x, int y) {
    int i = 0;
    while (this.spriteIndices[x][y][i] != NO_INDEX) {
      i++;
    }
    this.spriteIndices[x][y][i] = index;
  }

  /**
   * Moves a sprite index from one (x, y) coordinate to another.
   *
   * @param fromX The initial x coordinate.
   * @param fromY The initial y coordinate.
   * @param toX The final x coordinate.
   * @param toY The final y coordinate.
   * @param type The type of the sprite being moved. Assumes there will only be a single block or character at a single
   *             (x,y) position.
   */
  public void moveIndex(int fromX, int fromY, int toX, int toY, String type) {
    int i = 0;
    System.out.println(fromX);
    System.out.println(fromY);
    System.out.println(Arrays.toString(this.spriteIndices[fromX][fromY]));
    System.out.println(type);
    while (!this.sprites.get(this.spriteIndices[fromX][fromY][i]).getSpriteCategory().equals(type)) {
      i++;
    }
    int temp = this.spriteIndices[fromX][fromY][i];
    this.spriteIndices[fromX][fromY][i] = NO_INDEX;
    this.shiftDown(fromX, fromY);

    i = 0;
    while (this.spriteIndices[toX][toY][i] != NO_INDEX) {
      i++;
    }
    this.spriteIndices[toX][toY][i] = temp;
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

  /**
   * Records the (x,y) position of a target.
   *
   * @param target The target to be recorded
   */
  private void addTargetLocation(Sprite target) {
    int[] coordinates = new int[2];
    coordinates[0] = target.getxCell();
    coordinates[1] = target.getyCell();

    this.targetLocations.add(coordinates);
  }

  /**
   * Increments a cell coordinate in the specified direction.
   *
   * @param coordinate The coordinate to be incremented.
   * @param axis The axis the coordinate lies on.
   * @param direction The direction the movement is happening in.
   * @return The incremented coordinate.
   */
  private static int incrementCoordinate(int coordinate, char axis, Direction direction) {
    if (axis == 'x' && (direction == Direction.DIR_DOWN || direction == Direction.DIR_UP)) {
      // x coordinate cannot go up or down, so don't change it
      return coordinate;
    } else if (axis == 'y' && (direction == Direction.DIR_LEFT || direction == Direction.DIR_RIGHT)) {
      // y coordinate cannot go left or right, so don't change it
      return coordinate;
    } else if (axis == 'x' && direction == Direction.DIR_LEFT) {
      return --coordinate;
    } else if (axis == 'x' && direction == Direction.DIR_RIGHT) {
      return ++coordinate;
    } else if (axis == 'y' && direction == Direction.DIR_UP) {
      return --coordinate;
    } else if (axis == 'y' && direction == Direction.DIR_DOWN) {
      return ++coordinate;
    } else {
      // default case
      return coordinate;
    }
  }

  public int getTimer() {
    return this.timer;
  }

  public void setUpdatedThisFrame(boolean updatedThisFrame) {
    this.updatedThisFrame = updatedThisFrame;
  }

  @Override
  public void handlePlayerInput(Input input) {
    if (!input.isKeyPressed(Input.KEY_Z)) {
      return;
    }

    int lastUpdateTime = this.updateTimes.pop();
    if (lastUpdateTime == 0) {
      this.timer = 0;
      this.updateTimes.push(0);
      return;
    }

    for (Sprite sprite : this.sprites) {
      sprite.undo(lastUpdateTime);
    }
    this.spriteIndices = this.pastWorldLocations.get(lastUpdateTime);
    this.moveCount--;
  }

  @Override
  public void handlePlayerInput(Input input, World word) {
  }
}
