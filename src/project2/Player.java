package project2;

import org.newdawn.slick.Input;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * SWEN20003 Assignment 2B
 * <p>
 * Template taken from:
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 * <p>
 * Class for the Floor tile.
 *
 * @author Alex Vosnakis 743936
 */
public class Player extends Character implements Controllable {
  private HashMap<Integer, Position<Integer>> pastPositions;

  public Player(Position<Integer> cellPosition, Position<Float> windowPosition) {
    super("res/player_left.png", "player", cellPosition, windowPosition);
    pastPositions = new HashMap<>();
    addPastPosition(0);
  }

  /**
   * Update this sprite.
   *
   * @param keys  The keys the player has pressed.
   * @param delta The time since the last frame (ignored).
   * @param world The world this sprite is in.
   */
  @Override
  public void update(ArrayList<Integer> keys, int delta, World world) {
    handlePlayerInput(keys, world);
  }

  @Override
  public void handlePlayerInput(ArrayList<Integer> input) {
  }

  /**
   * Determines which direction the player is trying to move in, and attempts to move the player.
   *
   * @param keysPressed The arrow keys the player has pressed.
   * @param world       The world the Player is in.
   */
  @Override
  public void handlePlayerInput(ArrayList<Integer> keysPressed, World world) {
    Direction direction = determineDirection(keysPressed);
    if (direction == Direction.DIR_NONE) {
      return;
    }

    world.incrementMoves();
    // Move to our destination
    moveToDestination(direction, world);
  }

  /**
   * Attempt to move the player in the specified direction in the world. If it runs into a block, it will also
   * attempt to move that.
   *
   * @param direction The direction the player is attempting to move in.
   * @param world     The world the Player is in.
   */
  @Override
  public void moveToDestination(Direction direction, World world) {
    float speed = App.TILE_SIZE;
    int cellSpeed = GameUtils.CELL_SIZE;
    // Translate the direction to an x and y displacement
    float deltaX = GameUtils.directionDelta('x', direction, speed);
    float deltaY = GameUtils.directionDelta('y', direction, speed);
    int deltaXCell = GameUtils.directionDelta('x', direction, cellSpeed);
    int deltaYCell = GameUtils.directionDelta('y', direction, cellSpeed);


    addPastPosition(world.getTimer());
    world.setChangedThisFrame(true);

    Position<Integer> nextPosition = new Position<>(getxCell() + deltaXCell, getyCell() + deltaYCell);

    float nextX = getX() + deltaX;
    float nextY = getY() + deltaY;

    // If there is an enemy at the next location, restart the level
    if (world.spriteAtLocation(nextPosition, "character", Sprite.isOfCategory())) {
      world.reset();
      return;
    }

    // Special case for pushing TNT into walls. Try and find the potential NEXT position of the TNT being pushed,
    // and if it is a CrackedWall, push the TNT into it.
    if (world.spriteAtLocation(nextPosition, "tnt", Sprite.isOfType())) {
      Position<Integer> potentialWallPosition = new Position<>(
              nextPosition.x + deltaXCell,
              nextPosition.y + deltaYCell);
      // If it is a cracked wall, pushing the TNT into it
      if (world.spriteAtLocation(potentialWallPosition, "cracked", Sprite.isOfType())) {
        world.moveBlockAtPosition(nextPosition, direction);
      }
    }

    // Make sure the position isn't occupied!
    if (!world.isBlocked(nextPosition, direction)) {
      world.moveBlockAtPosition(nextPosition, direction);
      setCellPosition(nextPosition);
      snapToGrid(nextX, nextY);
    }
  }

  /**
   * Returns this sprite to the position it was at the specified time.
   *
   * @param time The time with the state we want the sprite to return to.
   */
  @Override
  public void undo(int time) {
    // If the block didn't update at this time, exit the method
    if (!pastPositions.containsKey(time)) {
      return;
    }

    // Get the coordinates of the specified time this block moved and update it
    setCellPosition(pastPositions.get(time));

    // Set the sprite's coordinates
    float newX = (float) Loader.getOffsetX() + getxCell() * App.TILE_SIZE;
    float newY = (float) Loader.getOffsetY() + getyCell() * App.TILE_SIZE;
    snapToGrid(newX, newY);

    pastPositions.remove(time);
  }

  /**
   * Save the current position to this Block's history, with the time as its key.
   *
   * @param time The timestamp to use a key.
   */
  @Override
  public void addPastPosition(int time) {
    pastPositions.put(time, new Position<>(getCellPosition()));
  }

  /**
   * Determines the Player direction based on the input. Essentially the directional controls for the game.
   *
   * @param keysPressed The Slick input object.
   * @return The direction that the player will try to move.
   */
  private static Direction determineDirection(ArrayList<Integer> keysPressed) {
    Direction direction = Direction.DIR_NONE;

    if (keysPressed.contains(Input.KEY_LEFT)) {
      direction = Direction.DIR_LEFT;
    } else if (keysPressed.contains(Input.KEY_RIGHT)) {
      direction = Direction.DIR_RIGHT;
    } else if (keysPressed.contains(Input.KEY_UP)) {
      direction = Direction.DIR_UP;
    } else if (keysPressed.contains(Input.KEY_DOWN)) {
      direction = Direction.DIR_DOWN;
    }
    return direction;
  }
}
