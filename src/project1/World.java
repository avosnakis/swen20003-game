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

  private int timer;
  private Stack<Integer> changeTimes;
  private boolean changedThisFrame;
  private int moveCount;

  public World() {
    currentLevel = 0;
    reset();
  }

  public void reset() {
    String filename = "res/levels/" + levels[currentLevel];
    sprites = Loader.loadSprites(filename);

    changedThisFrame = false;
    changeTimes = new Stack<>();
    changeTimes.push(0);

    timer = 0;
    moveCount = 0;
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

    // Update all sprites
    for (Sprite sprite : sprites) {
      if (sprite != null) {
        sprite.update(input, delta, this);
      }
    }

    // If there were changes in the world, save the past information
    if (changedThisFrame) {
      updateHistory();
    }
  }

  private void updateHistory() {
    changeTimes.push(timer);
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
    for (Sprite sprite : sprites) {
      if (sprite instanceof Target) {
        if (!((Target)sprite).isCovered()) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Determines whether an (x,y) position is possible to move to.
   *
   * @param position The (x,y) position to check.
   * @param direction The direction the sprite is currently moving.
   * @return Whether the position is blocked or not.
   */
  public boolean isBlocked(Position<Integer> position, Direction direction) {
    // Check that the block is moving to a spot inside the grid
    if (position.x < 0 ||
        position.x > Loader.getWorldWidth() ||
        position.y < 0 ||
        position.y > Loader.getWorldHeight()) {
      return false;
    }

    // Try to move to an (x, y) coordinate, checking every sprite at that location to determine the next action.
    boolean cannotMove = false;
    for (Sprite sprite : sprites) {
      if (sprite != null && sprite.isAtPosition(position)) {
        switch (sprite.getCategory()) {
          case "character":
            break;
          case "tile":
            cannotMove = !sprite.isPassable();
            break;
          case "block":
            int nextX = incrementCoordinate(position.x, 'x', direction);
            int nextY = incrementCoordinate(position.y, 'y', direction);
            cannotMove = isBlocked(new Position<>(nextX, nextY), direction);
            ((Block)sprite).moveToDestination(direction, this);
        }
      }
      if (cannotMove) {
        return true;
      }
    }
    return false;
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

    decrementMoves();
  }


  public boolean typeAtLocation(Position<Integer> position, String category) {
    for (Sprite sprite : sprites) {
      if (sprite != null && sprite.isAtPosition(position) && sprite.getType().equals(category)) {
        return true;
      }
    }
    return false;
  }

  public boolean categoryAtLocation(Position<Integer> position, String category) {
    for (Sprite sprite : sprites) {
      if (sprite != null && sprite.isAtPosition(position) && sprite.getCategory().equals(category)) {
        return true;
      }
    }
    return false;
  }


  /**
   * Purge the world history and world state of a sprite, and set it to null.
   *
   * @param position The position in the WorldState of the sprite.
   */
  public void destroySprite(Position<Integer> position) {
    for (int i = 0; i < sprites.size(); i++) {
      if (sprites.get(i) != null && sprites.get(i) instanceof Destructible && sprites.get(i).isAtPosition(position)) {
        sprites.set(i, null);
      }
    }
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
