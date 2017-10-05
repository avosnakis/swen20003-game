/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;

public class Sprite {

  private Image image = null;

  private Position<Integer> cellPosition;
  private Position<Float> windowPosition;

  private boolean passable;
  private String spriteCategory;
  private String spriteType;

  private HashMap<Integer, Position<Integer>> pastPositions;

  public Sprite(String imageSource, String spriteCategory, String spriteType, Position<Integer> cellPosition, Position<Float> windowPosition) {
    try {
      image = new Image(imageSource);
    } catch (SlickException e) {
      e.printStackTrace();
    }

    this.spriteCategory = spriteCategory;
    this.spriteType = spriteType;

    this.cellPosition = cellPosition;
    snapToGrid(windowPosition.x, windowPosition.y);

    pastPositions = new HashMap<>();
    addPastPosition(0);
  }

  public void update(Input input, int delta, World world) {
  }

  public void render(Graphics g) {
    image.drawCentered(windowPosition.x, windowPosition.y);
  }

  /**
   * Aligns the sprite's coordinates to the grid and sets the sprite's new windowCoordinates to this position.
   *
   * @param x The x coordinate to align.
   * @param y The y coordinate to align.
   */
  public void snapToGrid(float x, float y) {
    x /= App.TILE_SIZE;
    y /= App.TILE_SIZE;

    x = (float) Math.round(x);
    y = (float) Math.round(y);

    x *= App.TILE_SIZE;
    y *= App.TILE_SIZE;

    windowPosition = new Position<>(x, y, 0f);
  }

  /**
   * Attempts to move this sprite one tile in the specified direction.
   *
   * @param direction The direction in which the sprite is attempting to move.
   * @param world     The world in which the sprite is making this movement.
   */
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

    int nextXCell = this.getxCell() + deltaXCell;
    int nextYCell = this.getyCell() + deltaYCell;
    float nextX = this.getX() + deltaX;
    float nextY = this.getY() + deltaY;

    // Make sure the position isn't occupied!
    if (!world.isBlocked(nextXCell, nextYCell, direction)) {
      world.moveReference(cellPosition, nextXCell, nextYCell);
      snapToGrid(nextX, nextY);
    }
  }

  public void addPastPosition(int time) {
    pastPositions.put(time, new Position<>(cellPosition));
  }

  /**
   * Returns this sprite to the position it was at the specified time.
   *
   * @param time The time with the state we want the sprite to return to.
   */
  public void undo(int time) {
    // If the block didn't update at this time, exit the method
    if (!pastPositions.containsKey(time)) {
      return;
    }

    // Get the coordinates of the specified time this block moved and update it
    cellPosition = pastPositions.get(time);

    // Set the sprite's coordinates
    float newX = (float) Loader.getOffsetX() + getxCell() * App.TILE_SIZE;
    float newY = (float) Loader.getOffsetY() + getyCell() * App.TILE_SIZE;
    snapToGrid(newX, newY);

    pastPositions.remove(time);
  }

  public void setCellPosition(Position<Integer> cellPosition) {
    this.cellPosition = cellPosition;
  }

  public float getX() {
    return windowPosition.x;
  }

  public float getY() {
    return windowPosition.y;
  }

  public int getxCell() {
    return cellPosition.x;
  }

  public int getyCell() {
    return cellPosition.y;
  }

  public int getzCell() {
    return cellPosition.z;
  }

  public String getSpriteCategory() {
    return spriteCategory;
  }

  public String getSpriteType() {
    return spriteType;
  }

  public boolean isPassable() {
    return passable;
  }

  public void setPassable(boolean passable) {
    this.passable = passable;
  }

  public HashMap<Integer, Position<Integer>> getPastPositions() {
    return pastPositions;
  }

  public Position<Integer> getCellPosition() {
    return cellPosition;
  }
}
