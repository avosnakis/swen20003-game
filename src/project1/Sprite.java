/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;

public class Sprite {
  // Used to decide what direction an object is moving
  // Look up enums to find a more elegant solution!
  public static final int DIR_NONE = 0;
  public static final int DIR_LEFT = 1;
  public static final int DIR_RIGHT = 2;
  public static final int DIR_UP = 3;
  public static final int DIR_DOWN = 4;

  private Image image = null;
  private float x;
  private float y;

  public Sprite(String imageSource, float x, float y) {
    try {
      this.image = new Image(imageSource);
    } catch (SlickException e) {
      e.printStackTrace();
    }

    this.x = x;
    this.y = y;
    snapToGrid();
  }

  public void update(Input input, int delta) {

  }

  public void render(Graphics g) {
    this.image.drawCentered(x, y);
  }

  // Forces this sprite to align to the grid
  public void snapToGrid() {
    this.x /= App.TILE_SIZE;
    this.y /= App.TILE_SIZE;
    this.x = Math.round(x);
    this.y = Math.round(y);
    this.x *= App.TILE_SIZE;
    this.y *= App.TILE_SIZE;
  }

  public void moveToDest(int dir) {
    float speed = 32;
    // Translate the direction to an x and y displacement
    float deltaX = 0;
    float deltaY = 0;
    switch (dir) {
      case DIR_LEFT:
        deltaX = -speed;
        break;
      case DIR_RIGHT:
        deltaX = speed;
        break;
      case DIR_UP:
        deltaY = -speed;
        break;
      case DIR_DOWN:
        deltaY = speed;
        break;
    }

    // Make sure the position isn't occupied!
    if (!Loader.isBlocked(x + deltaX, y + deltaY)) {
      this.x += deltaX;
      this.y += deltaY;
    }
  }
}
