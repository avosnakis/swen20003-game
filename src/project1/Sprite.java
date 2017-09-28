/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;

public class Sprite implements Movable {

  private Image image = null;
  private float x;
  private float y;

  private int xCell;
  private int yCell;

  private boolean passable;
  private String spriteCategory;
  private String spriteType;

  public Sprite(String imageSource, String spriteCategory, String spriteType, float x, float y, int xCell, int yCell) {
    try {
      this.image = new Image(imageSource);
    } catch (SlickException e) {
      e.printStackTrace();
    }

    this.spriteCategory = spriteCategory;

    this.xCell = xCell;
    this.yCell = yCell;
    this.x = x;
    this.y = y;
    this.snapToGrid();
  }

  public void update(Input input, int delta, World world) {

  }

  public void render(Graphics g) {
    this.image.drawCentered(x, y);
  }

  /**
   * Aligns the sprite's coordinates to the grid.
   */
  public void snapToGrid() {
    this.x /= App.TILE_SIZE;
    this.y /= App.TILE_SIZE;
    this.x = Math.round(x);
    this.y = Math.round(y);
    this.x *= App.TILE_SIZE;
    this.y *= App.TILE_SIZE;
  }

  public float getX() {
    return this.x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return this.y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public int getxCell() {
    return this.xCell;
  }

  public void setxCell(int xCell) {
    this.xCell = xCell;
  }

  public int getyCell() {
    return this.yCell;
  }

  public void setyCell(int yCell) {
    this.yCell = yCell;
  }

  public String getSpriteCategory() {
    return this.spriteCategory;
  }

  public boolean isPassable() {
    return this.passable;
  }

  public void setPassable(boolean passable) {
    this.passable = passable;
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

    // Make sure the position isn't occupied!
    if (!world.isBlocked(this.xCell + deltaXCell, this.yCell + deltaYCell, direction)) {
      world.moveIndex(this.xCell,
          this.yCell,
          this.xCell + deltaXCell,
          this.yCell + deltaYCell,
          this.spriteCategory);

      this.x += deltaX;
      this.y += deltaY;

      this.xCell += deltaXCell;
      this.yCell += deltaYCell;

    }
  }
}
