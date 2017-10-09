package project1;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Character extends Sprite implements Movable {
  private HashMap<Integer, Position<Integer>> pastPositions;

  public Character(String imageSource, String spriteType, Position<Integer> cellPosition, Position<Float> windowPosition) {
    super(imageSource, "character", spriteType, cellPosition, windowPosition);

    pastPositions = new HashMap<>();
    addPastPosition(0);
  }

  @Override
  public void moveToDestination(Direction direction, World world) {
    float speed = 32;
    int cellSpeed = 1;
    // Translate the direction to an x and y displacement
    float deltaX = 0;
    float deltaY = 0;
    int deltaXCell = 0;
    int deltaYCell = 0;

    switch (direction) {
      case DIR_LEFT:
        deltaX = -speed;
        deltaXCell = -cellSpeed;
        break;
      case DIR_RIGHT:
        deltaX = speed;
        deltaXCell = cellSpeed;
        break;
      case DIR_UP:
        deltaY = -speed;
        deltaYCell = -cellSpeed;
        break;
      case DIR_DOWN:
        deltaY = speed;
        deltaYCell = cellSpeed;
        break;
    }

    addPastPosition(world.getTimer());
    world.setChangedThisFrame(true);

    Position<Integer> nextPosition = new Position<>(getxCell() + deltaXCell, getyCell() + deltaYCell);

    float nextX = this.getX() + deltaX;
    float nextY = this.getY() + deltaY;

    // Make sure the position isn't occupied!


    if (!world.isBlocked(nextPosition, direction)) {
      setCellPosition(nextPosition);
      snapToGrid(nextX, nextY);
    }
  }

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

  @Override
  public void addPastPosition(int time) {
    pastPositions.put(time, new Position<>(getCellPosition()));
  }


  /**
   * Determines whether the player attempted to move in this frame.
   *
   * @param keysPressed An array of all arrow keys the player pressed in this frame.
   * @return Whether or not the player pressed any arrow keys.
   */
  public static boolean playerMoved(ArrayList<Integer> keysPressed) {
    return keysPressed.size() > 0;
  }
}