/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

import java.util.*;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;


/**
 * Updates and renders all sprites, as well as having a record of the current positions visible on the screen
 * and all past positions on the screen. Also determines what type of blocks is at what coordinate, and manages
 * whether or not the player has won the level.
 */
public class World implements Controllable {

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
  private WorldState currentState;

  private int timer;
  private Stack<Integer> changeTimes;
  private HashMap<Integer, WorldState> pastStates;
  private boolean changedThisFrame;
  private int moveCount;

  public World() {
    currentLevel = 0;
    reset();
  }

  private void reset() {
    String filename = "res/levels/" + levels[currentLevel];
    sprites = Loader.loadSprites(filename);
    initialiseTargetLocations();

    currentState = new WorldState(sprites);

    changedThisFrame = false;
    changeTimes = new Stack<>();
    changeTimes.push(0);

    pastStates = new HashMap<>();
    pastStates.put(0, copyWorldState());

    timer = 0;
    moveCount = 0;
  }

  /**
   * Record all target (x, y, z) coordinates.
   */
  private void initialiseTargetLocations() {
    targetLocations = new ArrayList<>();
    for (Sprite sprite : sprites) {
      if (sprite.getType().equals("target")) {
        targetLocations.add(sprite.getCellPosition());
      }
    }
  }

  /**
   * Update all objects of the game, handle player input, and record the changed state of the world if so needed.
   *
   * @param input The Slick Input object, containing all input information.
   * @param delta The amount of time passed since the last frame.
   */
  public void update(Input input, int delta) {
    // If the player completed the level on the previous frame,
    // move to the next level and skip the rest of this frame
    if (hasWon()) {
      currentLevel += 1;
      reset();
      return;
    }

    // Check if the player tried to undo or reset
    handlePlayerInput(input);

    // Increment the internal timer, and make a record of the current world state
    timer += delta;
    WorldState currentWorldState = copyWorldState();

    // Update all sprites
    for (Sprite sprite : sprites) {
      if (sprite != null) {
        sprite.update(input, delta, this);
      }
    }

    // If there were changes in the world, save the past information
    if (changedThisFrame) {
      updateHistory(currentWorldState);
    }
  }

  private void updateHistory(WorldState currentWorldState) {
    changeTimes.push(timer);
    pastStates.put(timer, currentWorldState);
    changedThisFrame = false;

  }

  /**
   * Render all sprites and the move count to the screen.
   *
   * @param g The graphics object for the game.
   */
  public void render(Graphics g) {
    Tnt possibleTnt = null;
    for (Sprite sprite : sprites) {
      if (sprite != null && !(sprite instanceof Tnt)) {
        sprite.render(g);
      } else if (sprite != null) {
        // Check if the TNT here is exploding
        Tnt tnt = (Tnt)sprite;
        if (tnt.isExploding()) {
          possibleTnt = tnt;
        } else {
          sprite.render(g);
        }
      }
    }
    // If the TNT was exploding, render it last
    if (possibleTnt != null) {
      possibleTnt.render(g);
    }
    g.drawString(String.format("Moves: %d", moveCount), 0, 0);
  }

  /**
   * @return Whether all targets have been covered by a block.
   */
  public boolean hasWon() {
    boolean hasWon = false;
    for (Position<Integer> location : targetLocations) {
      hasWon = isCovered(location.x, location.y);
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
    for (int i = 0; i < WorldState.LENGTH && currentState.getValueAt(x, y, i) != WorldState.NO_INDEX; i++) {
      if (sprites.get(currentState.getValueAt(x, y, i)).getCategory().equals("block")) {
        return true;
      }
    }
    return false;
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

    // Try to move to an (x, y) coordinate, checking every sprite at that location to determine the next action.
    boolean cannotMove = false;
    for (int i = 0; i < WorldState.LENGTH && currentState.getValueAt(x, y, i) != WorldState.NO_INDEX; i++) {
      int index = currentState.getValueAt(x, y, i);
      switch (sprites.get(index).getCategory()) {
        case "character":
          break;
        // Determine whether the tile at the location is passable
        case "tile":
          cannotMove = !sprites.get(index).isPassable();
          break;
        // If it's a block, figure out if it can move, and if so, move it
        case "block":
          int nextX = incrementCoordinate(x, 'x', direction);
          int nextY = incrementCoordinate(y, 'y', direction);
          Block block = (Block) sprites.get(index);
          cannotMove = isBlocked(nextX, nextY, direction);
          block.moveToDestination(direction, this);
          break;
        default:
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
   * @param initialPosition The initial position of the sprite.
   * @param newX            The final x coordinate.
   * @param newY            The final y coordinate.
   */
  public void moveReference(Position<Integer> initialPosition, int newX, int newY) {
    int spriteIndex = currentState.getValueAt(initialPosition);
    int newZ = currentState.findEmptyZ(newX, newY);

    Position<Integer> finalPosition = new Position<>(newX, newY, newZ);
    currentState.moveIndex(initialPosition, finalPosition);
    sprites.get(spriteIndex).setCellPosition(finalPosition);
  }

  @Override
  public void handlePlayerInput(Input input) {
    if (input.isKeyPressed(Input.KEY_R)) {
      reset();
      return;
    }
    if (input.isKeyPressed(Input.KEY_Z)) {
      undo();
    }
  }

  @Override
  public void handlePlayerInput(Input input, World word) {
  }

  /**
   * Set the world back to the last time a player made an input.
   */
  private void undo() {
    int lastUpdateTime = changeTimes.pop();

    // If the world is in its initial state, do nothing
    if (lastUpdateTime == 0) {
      timer = 0;
      changeTimes.push(0);
      return;
    }

    // Undo all sprites
    for (Sprite sprite : sprites) {
      if (sprite != null && sprite instanceof Movable) {
        if (sprite instanceof Block) {
          ((Block)sprite).undo(lastUpdateTime);
        } else {
          ((Character)sprite).undo(lastUpdateTime);
        }
      }
    }

    // Set the world state back and decrement the move count
    currentState = pastStates.get(lastUpdateTime);
    decrementMoves();
  }

  /**
   * Determines whether there is a cracked wall at an (x, y) coordinate.
   *
   * @param x The x coordinate of the location to check.
   * @param y The y coordinate of the location to check.
   * @return -1 if there is no cracked wall, otherwise the z coordinate of the cracked wall in the WorldState.
   */
  public int crackedWallAtLocation(int x, int y) {
    for (int i = 0; i < WorldState.LENGTH; i++) {
      int index = currentState.getValueAt(x, y, i);
      if (index != WorldState.NO_INDEX && sprites.get(index).getType().equals("cracked")) {
        return i;
      }
    }
    return WorldState.NO_INDEX;
  }

  /**
   * Purge the world history and world state of a sprite, and set it to null.
   *
   * @param position The position in the WorldState of the sprite.
   */
  public void destroySprite(Position<Integer> position) {
    // TODO when destroying a sprite at an (x,y), update the positions of each of the sprites at that (x,y) to have
    // the new Z
    int index = currentState.getValueAt(position);
    currentState.removeValue(position);

    if (sprites.get(index) instanceof Tnt) {
      clearHistoryOfSprite(((Tnt)sprites.get(index)).getPastPositions());
    } else {
      clearHistoryOfSprite(sprites.get(index).getCellPosition());
    }
    sprites.set(index, null);
  }

  /**
   * Purges the World history of all references to an object.
   *
   * @param spritePastPositions The past positions of the sprite.
   */
  private void clearHistoryOfSprite(HashMap<Integer, Position<Integer>> spritePastPositions) {
    Iterator<Integer> iterator = changeTimes.iterator();

    // Initialise the x and y to the first position
    int time = iterator.next();
    Position<Integer> currentPosition = spritePastPositions.get(time);

    // Move through the World history by timestamp and remove every reference to this block
    while (iterator.hasNext()) {
      pastStates.get(time).removeValue(currentPosition);
      // Increment to the next world state, and if the sprite was changed then, move to that position
      time = iterator.next();
      if (spritePastPositions.containsKey(time)) {
        currentPosition = spritePastPositions.get(time);
      }
    }
  }

  /**
   * Purge the world history of a sprite at a particular location.
   *
   *  @param position The position to purge.
   */
  private void clearHistoryOfSprite(Position<Integer> position) {
    Iterator<Integer> iterator = changeTimes.iterator();

    while (iterator.hasNext()) {
      int time = iterator.next();
      pastStates.get(time).removeValue(position);
    }
  }

  /**
   * Manually instantiate a new WorldState with exactly the same data as the current state.
   *
   * @return A new instance of the current sprite indices array as-is.
   */
  private WorldState copyWorldState() {
    return new WorldState(currentState);
  }

  public int getTimer() {
    return timer;
  }

  public void setChangedThisFrame(boolean changedThisFrame) {
    this.changedThisFrame = changedThisFrame;
  }

  public void incrementMoves() {
    moveCount++;
  }

  public void decrementMoves() {
    moveCount--;
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
}
