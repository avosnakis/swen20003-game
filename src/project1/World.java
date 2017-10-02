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

  private String levelFile;

  private ArrayList<Sprite> sprites;
  private ArrayList<int[]> targetLocations;
  private int[][][] spriteIndices;

  private int timer;
  private Stack<Integer> changeTimes;
  private HashMap<Integer, int[][][]> pastWorldLocations;
  private boolean changedThisFrame;
  private int moveCount;

  public World(String levelFile) {
    this.sprites = Loader.loadSprites(levelFile);
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

    this.changedThisFrame = false;
    this.changeTimes = new Stack<>();
    this.changeTimes.push(0);

    this.pastWorldLocations = new HashMap<>();
    this.pastWorldLocations.put(0, this.spriteIndices.clone());

    this.timer = 0;
    this.moveCount = 0;

    this.levelFile = levelFile;
  }

  /**
   * Update all objects of the game, handle player input, and record the changed state of the world if so needed.
   *
   * @param input
   * @param delta
   */
  public void update(Input input, int delta) {

    // Check if the player tried to undo
    this.handlePlayerInput(input);

    // Increment the internal timer, and make a record of the current world state
    this.timer += delta;
    int[][][] currentSpriteIndices = this.copySpriteIndices();

    // Update all sprites
    for (Sprite sprite : this.sprites) {
      if (sprite != null) {
        sprite.update(input, delta, this);
      }
    }

    // If there were changes in the world, save the past information
    if (this.changedThisFrame) {
      this.changeTimes.push(this.timer);
      this.pastWorldLocations.put(this.timer, currentSpriteIndices);
      this.changedThisFrame = false;
    }
  }

  /**
   * Render all sprites and the move count to the screen.
   *
   * @param g The graphics object for the game.
   */
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
   * @param x         The x point to be checked.
   * @param y         The y point to be checked.
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
          cannotMove = this.isBlocked(nextX, nextY, direction);
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
   * @param toX   The final x coordinate.
   * @param toY   The final y coordinate.
   * @param type  The type of the sprite being moved. Assumes there will only be a single block or character at a single
   *              (x,y) position.
   */
  public void moveIndex(int fromX, int fromY, int toX, int toY, String type) {
    int i = 0;
    // Find the index of block or character we want to move
    while (!this.sprites.get(this.spriteIndices[fromX][fromY][i]).getSpriteCategory().equals(type)) {
      i++;
    }
    // Remove its index
    int temp = this.spriteIndices[fromX][fromY][i];
    this.spriteIndices[fromX][fromY][i] = NO_INDEX;
    this.shiftDown(fromX, fromY);

    // Place it in its new location
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
   * @param axis       The axis the coordinate lies on.
   * @param direction  The direction the movement is happening in.
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

  @Override
  public void handlePlayerInput(Input input) {
    if (input.isKeyPressed(Input.KEY_Z)) {
      this.undo();
    }
  }

  @Override
  public void handlePlayerInput(Input input, World word) {
  }

  /**
   * Set the world back to the last time a player made an input.
   */
  private void undo() {
    int lastUpdateTime = this.changeTimes.pop();

    // If the world is in its initial state, do nothing
    if (lastUpdateTime == 0) {
      this.timer = 0;
      this.changeTimes.push(0);
      return;
    }

    // Undo all sprites
    for (Sprite sprite : this.sprites) {
      if (sprite != null) {
        sprite.undo(lastUpdateTime);
      }
    }

    // Set the world state back and decrement the move count
    this.spriteIndices = this.pastWorldLocations.get(lastUpdateTime);
    this.moveCount--;
  }

  public int crackedWallAtLocation(int x, int y) {
    int i = 0;
    while (this.spriteIndices[x][y][i] != NO_INDEX && i < this.spriteIndices[x][y].length) {
      if (this.sprites.get(this.spriteIndices[x][y][i]).getSpriteType().equals("cracked")) {
        return i;
      }
      i++;
    }
    return -1;
  }

  public void destroyWall(int x, int y, int z) {
    int index = this.spriteIndices[x][y][z];
    this.sprites.set(index, null);
    this.spriteIndices[x][y][z] = NO_INDEX;
    this.shiftDown(x, y);
  }

  /**
   * Destroys a TNT block at an (x, y) coordinate.
   *
   * @param x The x coordinate of the TNT to destroy.
   * @param y The y coordinate of the TNT to destroy.
   */
  public void destroyTnt(int x, int y) {
    int i = 0;
    while (this.spriteIndices[x][y][i] != NO_INDEX && i < this.spriteIndices[x][y].length) {
      if (this.sprites.get(this.spriteIndices[x][y][i]).getSpriteType().equals("tnt")) {
        break;
      }
      i++;
    }

    this.sprites.set(this.spriteIndices[x][y][i], null);
    this.spriteIndices[x][y][i] = NO_INDEX;
    this.shiftDown(x, y);
  }

  /**
   * Manually instantiate a new 3D array of sprite indices, and copy the contents of the old one to in.
   *
   * @return A new instance of the current sprite indices array as-is.
   */
  private int[][][] copySpriteIndices() {
    int[][][] newSpriteIndices = new int[Loader.getWorldWidth()][Loader.getWorldHeight()][5];
    for (int i = 0; i < Loader.getWorldWidth(); i++) {
      for (int j = 0; j < Loader.getWorldHeight(); j++) {
        System.arraycopy(this.spriteIndices[i][j],
            0,
            newSpriteIndices[i][j],
            0,
            this.spriteIndices[i][j].length);
      }
    }
    return newSpriteIndices;
  }

  public int getTimer() {
    return this.timer;
  }

  public void setChangedThisFrame(boolean changedThisFrame) {
    this.changedThisFrame = changedThisFrame;
  }
}
