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

public class Sprite implements Movable {

  private Image image = null;

  private Position<Integer> cellPosition;
  private Position<Float> windowPosition;

  private boolean passable;
  private String spriteCategory;
  private String spriteType;

  private HashMap<Integer, Position<Integer>> pastPositions;

  public Sprite(String imageSource, String spriteCategory, String spriteType, Position<Integer> cellPosition, Position<Float> windowPosition) {
    try {
      this.image = new Image(imageSource);
    } catch (SlickException e) {
      e.printStackTrace();
    }

    this.spriteCategory = spriteCategory;
    this.spriteType = spriteType;

    this.cellPosition = cellPosition;
    this.windowPosition = windowPosition;
    this.snapToGrid();

    this.pastPositions = new HashMap<>();
    this.addPastPosition(0);
  }

  public void update(Input input, int delta, World world) {

  }

  public void render(Graphics g) {
    this.image.drawCentered(this.windowPosition.getX(), this.windowPosition.getY());
  }

  /**
   * Aligns the sprite's coordinates to the grid.
   */
  public void snapToGrid() {
    this.setX(this.getX() / App.TILE_SIZE);
    this.setY(this.getY() / App.TILE_SIZE);

    this.setX((float)Math.round(this.getX()));
    this.setY((float)Math.round(this.getY()));

    this.setX(this.getX() * App.TILE_SIZE);
    this.setY(this.getY() * App.TILE_SIZE);
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

    this.addPastPosition(world.getTimer());
    world.setChangedThisFrame(true);

    int nextXCell = this.getxCell() + deltaXCell;
    int nextYCell = this.getyCell() + deltaYCell;
    float nextX = this.getX() + deltaX;
    float nextY = this.getY() + deltaY;

    // Make sure the position isn't occupied!
    if (!world.isBlocked(nextXCell, nextYCell, direction)) {
      world.moveIndex(this.getxCell(), this.getyCell(), this.getzCell(), nextXCell, nextYCell);

      this.setX(nextX);
      this.setY(nextY);

      this.setxCell(nextXCell);
      this.setyCell(nextYCell);
    }
  }

  public void addPastPosition(int time) {
    this.pastPositions.put(time, new Position<>(this.cellPosition));
  }

  public void undo(int time) {
    // If the block didn't update at this time, exit the method
    if (!this.pastPositions.containsKey(time)) {
      return;
    }

    // Get the coordinates of the specified time this block moved and update it
    this.cellPosition = this.pastPositions.get(time);

    // Set the sprite's coordinates
    this.setX((float)Loader.getOffsetX() + this.getxCell() * App.TILE_SIZE);
    this.setY((float)Loader.getOffsetY() + this.getyCell() * App.TILE_SIZE);
    this.snapToGrid();

    this.pastPositions.remove(time);
  }

  public float getX() {
    return this.windowPosition.getX();
  }

  public void setX(float x) {
    this.windowPosition.setX(x);
  }

  public float getY() {
    return this.windowPosition.getY();
  }

  public void setY(float y) {
    this.windowPosition.setY(y);
  }

  public int getxCell() {
    return this.cellPosition.getX();
  }

  public void setxCell(int xCell) {
    this.cellPosition.setX(xCell);
  }

  public int getyCell() {
    return this.cellPosition.getY();
  }

  public void setyCell(int yCell) {
    this.cellPosition.setY(yCell);
  }

  public int getzCell() {
    return this.cellPosition.getZ();
  }

  public void setzcell(int z) {
    this.cellPosition.setZ(z);
  }

  public String getSpriteCategory() {
    return this.spriteCategory;
  }

  public String getSpriteType() {
    return this.spriteType;
  }

  public boolean isPassable() {
    return this.passable;
  }

  public void setPassable(boolean passable) {
    this.passable = passable;
  }

  public HashMap<Integer, Position<Integer>> getPastPositions() {
    return this.pastPositions;
  }

  public Position<Integer> getCellPosition() {
    return this.cellPosition;
  }
}
