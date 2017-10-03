/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Stack;
import java.util.HashMap;
import java.util.Iterator;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class World implements Controllable {
  private static final int NO_INDEX = -1;

  private static final String[] levels = {
      "0.lvl",
      "1.lvl",
      "2.lvl",
      "3.lvl",
      "4.lvl",
      "5.lvl",
      "6.lvl"
  };

  private int currentLevel;

  private ArrayList<Sprite> sprites;
  private ArrayList<Position<Integer>> targetLocations;
  private int[][][] spriteIndices;

  private int timer;
  private Stack<Integer> changeTimes;
  private HashMap<Integer, int[][][]> pastWorldLocations;
  private boolean changedThisFrame;
  private int moveCount;

  public World() {
    this.currentLevel = 0;
    this.reset(0);
  }

  private void reset(int level) {
    String filename = "res/levels/" + levels[level];
    this.sprites = Loader.loadSprites(filename);
    this.targetLocations = new ArrayList<>();

    this.initialiseSpriteIndices();
    this.initialiseTargetLocations();

    this.changedThisFrame = false;
    this.changeTimes = new Stack<>();
    this.changeTimes.push(0);

    this.pastWorldLocations = new HashMap<>();
    this.pastWorldLocations.put(0, this.copySpriteIndices());

    this.timer = 0;
    this.moveCount = 0;
  }

  private void initialiseSpriteIndices() {
    this.spriteIndices = new int[Loader.getWorldWidth()][Loader.getWorldHeight()][5];
    for (int[][] plane : this.spriteIndices) {
      for (int[] row : plane) {
        Arrays.fill(row, NO_INDEX);
      }
    }
    for (int i = 0; i < this.sprites.size(); i++) {
      Sprite sprite = this.sprites.get(i);
      this.spriteIndices[sprite.getxCell()][sprite.getyCell()][sprite.getzCell()] = i;
    }
  }

  private void initialiseTargetLocations() {
    for (int i = 0; i < this.sprites.size(); i++) {
      if (this.sprites.get(i).getSpriteType().equals("target")) {
        this.addTargetLocation(this.sprites.get(i));
      }
    }
  }

  /**
   * Update all objects of the game, handle player input, and record the changed state of the world if so needed.
   *
   * @param input
   * @param delta
   */
  public void update(Input input, int delta) {
    // If the player completed the level on the previous frame,
    // move to the next level and skip the rest of this frame
    if (this.hasWon()) {
      this.currentLevel += 1;
      this.reset(this.currentLevel);
      return;
    }

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
    for (Position<Integer> location : this.targetLocations) {
      hasWon = this.isCovered(location.getX(), location.getY());
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

  public void decrementMoves() {
    this.moveCount--;
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
    // Check that the block is moving to a spot inside the grid
    if (x < 0 || x > Loader.getWorldWidth() || y < 0 || y > Loader.getWorldHeight()) {
      return false;
    }
    System.out.println(direction);
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
   * Moves a sprite index from one (x, y) coordinate to another.
   *
   * @param fromX The initial x coordinate.
   * @param fromY The initial y coordinate.
   * @param fromZ The initial z coordinate.
   * @param toX   The final x coordinate.
   * @param toY   The final y coordinate.
   */
  public void moveIndex(int fromX, int fromY, int fromZ, int toX, int toY) {
    // Remove its index
    int spriteIndex = this.spriteIndices[fromX][fromY][fromZ];
    this.spriteIndices[fromX][fromY][fromZ] = NO_INDEX;
    this.shiftDown(fromX, fromY);

    // Place it in its new location
    int newZ = 0;
    while (this.spriteIndices[toX][toY][newZ] != NO_INDEX) {
      newZ++;
    }
    this.spriteIndices[toX][toY][newZ] = spriteIndex;
    this.sprites.get(spriteIndex).setzcell(newZ);
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
   * Shifts the z-axis at a specified coordinate, so that all empty slots are at the top of the matrix.
   *
   * @param x The x-coordinate of the z-axis to shift.
   * @param y The y-coordinate of the z-axis to shift.
   */
  private void shiftDown(int x, int y, int[][][] matrix) {
    for (int i = 0; i < matrix[x][y].length - 1; i++) {
      if (matrix[x][y][i] == NO_INDEX) {
        matrix[x][y][i] = matrix[x][y][i + 1];
        matrix[x][y][i + 1] = NO_INDEX;
      }
    }
  }

  /**
   * Records the (x,y) position of a target.
   *
   * @param target The target to be recorded
   */
  private void addTargetLocation(Sprite target) {
    this.targetLocations.add(target.getCellPosition());
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
    if (input.isKeyPressed(Input.KEY_R)) {
      this.reset(this.currentLevel);
      return;
    }
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
    this.decrementMoves();
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

  public void destroySprite(int x, int y, int z) {
    int index = this.spriteIndices[x][y][z];
    this.clearReferences(this.sprites.get(index).getPastPositions(), index);
    this.sprites.set(index, null);
    this.spriteIndices[x][y][z] = NO_INDEX;
    this.shiftDown(x, y);
  }

  /**
   * Purges the World history of all references to an object.
   *
   * @param spritePastPositions The past positions of the sprite.
   * @param index               The sprite's index in the sprites ArrayList.
   */
  private void clearReferences(HashMap<Integer, Position<Integer>> spritePastPositions, int index) {
    Iterator<Integer> iterator = this.changeTimes.iterator();

    // Initialise the x and y to the first position
    int time = iterator.next();
    int currentX = spritePastPositions.get(time).getX();
    int currentY = spritePastPositions.get(time).getY();
    int currentZ = spritePastPositions.get(time).getZ();

    // Move through the World history by timestamp and remove every reference to this block
    while (iterator.hasNext()) {
      this.pastWorldLocations.get(time)[currentX][currentY][currentZ] = NO_INDEX;
      shiftDown(currentX, currentY, this.pastWorldLocations.get(time));

      // Increment to the next world state, and if the sprite was changed then, update the positions to update
      time = iterator.next();
      if (spritePastPositions.containsKey(time)) {
        currentX = spritePastPositions.get(time).getX();
        currentY = spritePastPositions.get(time).getY();
        currentZ = spritePastPositions.get(time).getZ();
      }
    }
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
