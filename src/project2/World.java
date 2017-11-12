package project2;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;
import java.util.function.BiPredicate;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Template taken from:
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 * <p>
 * Updates and renders all sprites, including control over the level reset and undo move functionality.
 * Given its access to every sprite, it also has logic to find certain information about sprites (or the sprites
 * themselves) for other sprites.
 *
 * @author Alex Vosnakis 743936
 */
public class World implements Controllable {

  private static final String[] levels = {"0.lvl", "1.lvl", "2.lvl", "3.lvl", "4.lvl", "5.lvl"};
  private static int finalLevel = levels.length - 1;

  private int currentLevel;
  private int moveCount;

  private ArrayList<Sprite> sprites;

  private Timer timer;
  private Stack<Integer> changeTimes;
  private boolean changedThisFrame;

  public World() {
    currentLevel = 0;
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
    // Determine which of the controls the player used
    ArrayList<Integer> arrowKeys = GameUtils.getPressedKeys(input,
        Input.KEY_DOWN, Input.KEY_LEFT, Input.KEY_RIGHT, Input.KEY_UP);
    ArrayList<Integer> otherKeys = GameUtils.getPressedKeys(input, Input.KEY_R, Input.KEY_Z);

    // If the player completed the level on the previous frame, move to the next level and skip the rest of this frame
    if (hasWon()) {
      currentLevel += 1;
      reset();
      return;
    }

    // Check if the player tried to undo or reset
    if (GameUtils.playerMoved(otherKeys)) {
      handlePlayerInput(otherKeys);
      return;
    }

    // Increment the internal timer and update every non-null sprite
    timer.tick(delta);
    sprites.stream()
        .filter(Objects::nonNull)
        .forEach(sprite -> sprite.update(arrowKeys, delta, this));

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
    // Find any TNT that is currently exploding
    Tnt possibleTnt = (Tnt) sprites.stream()
        .filter(sprite -> sprite instanceof Tnt && ((Tnt) sprite).isUndetonated())
        .findFirst()
        .orElse(null);
    // Render all sprites that aren't exploding Tnt
    sprites.stream()
        .filter(Objects::nonNull)
        .forEach(sprite -> sprite.render(g));

    // If the TNT was exploding, render it last
    if (possibleTnt != null) {
      possibleTnt.render(g);
    }
    g.drawString(String.format("Moves: %d", moveCount), 0, 0);
  }

  /**
   * @return Whether all targets have been covered by a block, and it is not the final level.
   */
  private boolean hasWon() {
    return currentLevel != finalLevel &&
        sprites.stream()
            .filter(sprite -> sprite instanceof Target)
            .allMatch(sprite -> ((Target) sprite).isCovered());
  }

  /**
   * Determines whether an (x,y) position is possible to move to. ie That it is moving to somewhere inside the grid
   * and that position is not occupied by an impassable block.
   *
   * @param position  The (x,y) position to check.
   * @param direction The direction the sprite is currently moving.
   * @return Whether the position is blocked or not.
   */
  public boolean isBlocked(Position<Integer> position, Direction direction) {
    // First check that we are within the bounds.
    return position.x >= 0 &&
        position.x <= Loader.getWorldWidth() &&
        position.y >= 0 &&
        position.y <= Loader.getWorldHeight() &&
        // Check there are no tiles that are blocked.
        sprites.stream()
            .filter(sprite -> sprite != null && sprite.isAtPosition(position))
            .anyMatch(sprite -> {
              switch (sprite.getCategory()) {
                case "character":
                case "tile":
                  return !sprite.isPassable();
                // Check if the next block can move
                case "block":
                  int nextX = GameUtils.incrementCoordinate(position.x, 'x', direction);
                  int nextY = GameUtils.incrementCoordinate(position.y, 'y', direction);
                  return isBlocked(new Position<>(nextX, nextY), direction);
                default:
                  return true;
              }
            });
  }

  /**
   * If there is a block at the specified position, move it in the specified direction. By this point we already
   * know the block can move. Assumes there can only be one block at one position.
   *
   * @param position  The position of the block to move.
   * @param direction The direction to move the block in.
   */
  public void moveBlockAtPosition(Position<Integer> position, Direction direction) {
    Block block = ((Block) sprites.stream()
        .filter(sprite -> sprite instanceof Block && sprite.isAtPosition(position))
        .findFirst()
        .orElse(null));
    if (block != null) {
      block.moveToDestination(direction, this);
    }
  }

  /**
   * If the player has pressed the appropriate keys, undo or reset the world.
   *
   * @param input The keys the player has pressed.
   */
  @Override
  public void handlePlayerInput(ArrayList<Integer> input) {
    if (input.contains(Input.KEY_R)) {
      reset();
    } else if (input.contains(Input.KEY_Z)) {
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

    // Try to undo all Movable sprites, ie. Blocks and Characters
    sprites.stream()
        .filter(sprite -> sprite instanceof Movable)
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
   * Determines if a sprite of a specific type or category is at an (x,y) coordinate.
   *
   * @param position   The (x,y) coordinate to check.
   * @param type       The type OR category of the sprite to check.
   * @param spriteType The function testing the sprite test or category.
   * @return Whether a sprite of a given type OR category is at that position.
   */
  public boolean spriteAtLocation(Position<Integer> position, String type, BiPredicate<Sprite, String> spriteType) {
    return sprites.stream()
        .anyMatch(sprite -> sprite != null &&
            sprite.isAtPosition(position) &&
            spriteType.test(sprite, type));
  }

  /**
   * Finds a sprite of a certain type in a level. Returns the first one found so assumes there is only one.
   *
   * @return The sprite if it is in the level, null otherwise.
   */
  public Sprite findUniqueSprite(String type) {
    return sprites.stream()
        .filter(sprite -> sprite.getType().equals(type))
        .findFirst()
        .orElse(null);
  }

  /**
   * Finds the player's location.
   *
   * @return The player's Position, null if there is no player.
   */
  public Position<Integer> getPlayerPosition() {
    return sprites.stream()
        .filter(sprite -> sprite instanceof Player)
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