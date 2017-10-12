/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project2;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public class Sprite {

  private Image image = null;

  private Position<Integer> cellPosition;
  private Position<Float> windowPosition;

  private boolean passable;
  private String category;
  private String type;

  public Sprite(String image, String category, String type, Position<Integer> cellPosition, Position<Float> windowPosition) {
    try {
      this.image = new Image(image);
    } catch (SlickException e) {
      e.printStackTrace();
    }

    this.category = category;
    this.type = type;

    this.cellPosition = cellPosition;
    snapToGrid(windowPosition.x, windowPosition.y);
  }

  public void update(ArrayList<Integer> keysPressed, int delta, World world) {
  }

  /**
   * Render the sprite to the screen.
   *
   * @param g The Slick graphics object.
   */
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

    windowPosition = new Position<>(x, y);
  }

  public boolean isAtPosition(Position<Integer> position) {
    return cellPosition.equals(position);
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

  public String getCategory() {
    return category;
  }

  public String getType() {
    return type;
  }

  public boolean isPassable() {
    return passable;
  }

  public void setPassable(boolean passable) {
    this.passable = passable;
  }

  public Position<Integer> getCellPosition() {
    return cellPosition;
  }

  @Override
  public String toString() {
    return "Sprite{" +
        "cellPosition=" + cellPosition +
        ", windowPosition=" + windowPosition +
        ", passable=" + passable +
        ", category='" + category + '\'' +
        ", type='" + type + '\'' +
        '}';
  }
}
