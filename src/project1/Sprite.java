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
    this.snapToGrid();
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
}
