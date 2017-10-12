/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project2;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * Updates and renders all sprites. Also determines what type of blocks is at what coordinate, and manages
 * whether or not the player has won the level.
 */
public class World implements Controllable {

  private static final String[] levels = {"0.lvl", "1.lvl", "2.lvl", "3.lvl", "4.lvl", "5.lvl"};

  private int currentLevel;

  private ArrayList<Sprite> sprites;

  private Timer timer;
  private Stack<Integer> changeTimes;
  private boolean changedThisFrame;

  private int moveCount;

  public World() {
    currentLevel = 4;
    reset();
  }

  /**
   * Reinitialise this level from the levelfile.
   */
  public void reset() {
    String filename = "res/levels/" + levels[currentLevel];
    sprites = Loader.loadSprites(filename);

    changedThisFrame = false;
    changeTimes = new Stack<>();
    changeTimes.push(0);

    timer = new Timer();
    moveCount = 0;
  }

  /**
   * Update all objects of the game, handle player input, and record the changed state of the world if so needed.
   *
   * @param input The Slick Input object, containing all input information.
   * @param delta The amount of time passed since the last frame.
   */
  public void update(Input input, int delta) {
    ArrayList<Integer> arrowKeysPressed = GameUtils.recordArrowKeysPressed(input);

    // If the player completed the level on the previous frame,
    // move to the next level and skip the rest of this frame
    if (hasWon()) {
      currentLevel += 1;
      reset();
      return;
    }

    // Check if the player tried to undo or reset
    handlePlayerInput(input);

    // Increment the internal timer
    timer.tick(delta);

    // Update every non-null sprite
    sprites.stream()
        .filter(Objects::nonNull)
        .forEach(sprite -> sprite.update(arrowKeysPressed, delta, this));

    // If there were changes in the world, save the past information
    if (changedThisFrame) {
      updateHistory();
    }
  }

  /**
   * Adds the current time counter to the changeTimes stack and reset the changed status.
   */
  private void updateHistory() {
    changeTimes.push(timer.getCounter());
    changedThisFrame = false;
  }

  /**
   * Render all sprites and the move count to the screen.
   * If a TNT block is exploding, render it last.
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
        Tnt tnt = (Tnt) sprite;
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
   * @return Whether all targets have been covered by a block, and it is not the final level.
   */
  public boolean hasWon() {
    return currentLevel != levels.length - 1 &&
        sprites.stream()
            .filter(sprite -> sprite instanceof Target)
            .allMatch(sprite -> ((Target) sprite).isCovered());
  }

  /**
   * Determines whether an (x,y) position is possible to move to.
   * If a block is at the specified (x,y), then we attempt to move that one as well.
   *
   * @param position  The (x,y) position to check.
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
          // Determine whether the tile is passable
          case "tile":
            cannotMove = !sprite.isPassable();
            break;
          // Check if the next block can move, and attempt to move it
          case "block":
            int nextX = GameUtils.incrementCoordinate(position.x, 'x', direction);
            int nextY = GameUtils.incrementCoordinate(position.y, 'y', direction);
            cannotMove = isBlocked(new Position<>(nextX, nextY), direction);
            ((Block) sprite).moveToDestination(direction, this);
        }
      }
      // Exit as soon as it is known the next (x,y) is impassable
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
    } else if (input.isKeyPressed(Input.KEY_Z)) {
      undo();
    }
  }

  @Override
  public void handlePlayerInput(ArrayList<Integer> keysPressed, World world) {
  }

  /**
   * Set the world back to the last time a player made an input.
   */
  private void undo() {
    int lastUpdateTime = changeTimes.pop();

    // If the world is in its initial state, do nothing and exit the method
    if (lastUpdateTime == 0) {
      timer.reset();
      changeTimes.push(0);
      return;
    }

    // Try to undo all movable sprites
    sprites.stream()
        .filter(sprite -> sprite != null && sprite instanceof Movable)
        .forEach(sprite -> {
          if (sprite instanceof Block) {
            ((Block) sprite).undo(lastUpdateTime);
          } else {
            ((Character) sprite).undo(lastUpdateTime);
          }
        });
    decrementMoves();
  }

  /**
   * Determines if a sprite of the given category is at an (x,y) coordinate.
   *
   * @param position The (x,y) coordinate to check.
   * @param type     The type to check the (x,y) coordinates for.
   * @return True if there is a sprite of the given category at the (x,y) location, otherwise false.
   */
  public boolean typeAtLocation(Position<Integer> position, String type) {
    return sprites.stream()
        .anyMatch(sprite -> sprite != null &&
            sprite.isAtPosition(position) &&
            sprite.getType().equals(type));
  }

  /**
   * Determines if a sprite of the given type is at an (x,y) coordinate.
   *
   * @param position The (x,y) coordinate to check.
   * @param category The type to check the (x,y) coordinates for.
   * @return True if there is a sprite of the given type at the (x,y) location, otherwise false.
   */
  public boolean categoryAtLocation(Position<Integer> position, String category) {
    return sprites.stream()
        .anyMatch(sprite -> sprite != null &&
            sprite.isAtPosition(position) &&
            sprite.getCategory().equals(category));
  }

  /**
   * Finds the Door in the level, as there can only be a single one.
   *
   * @return The door if it is in the level, null otherwise.
   */
  public Door findDoor() {
    return (Door) sprites.stream()
        .filter(sprite -> sprite != null && sprite.getType().equals("door"))
        .findFirst()
        .orElse(null);
  }

  /**
   * Set the specified sprite to null in the sprites ArrayList. Assumes there can only be one destructible block
   * at one (x,y) location.
   *
   * @param position The (x,y) coordinate of the sprite.
   */
  public void destroySprite(Position<Integer> position) {
    for (int i = 0; i < sprites.size(); i++) {
      if (sprites.get(i) != null && sprites.get(i) instanceof Destructible && sprites.get(i).isAtPosition(position)) {
        sprites.set(i, null);
        break;
      }
    }
  }

  /**
   * Finds the player's location.
   *
   * @return The player's Position, null if there is no player.
   */
  public Position<Integer> getPlayerPosition() {
    return sprites.stream()
        .filter(sprite -> sprite != null && sprite.getType().equals("player"))
        .map(Sprite::getCellPosition)
        .findFirst()
        .orElse(null);
  }

  public int getTimer() {
    return timer.getCounter();
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
}