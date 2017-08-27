package project1;

import org.lwjgl.Sys;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import static project1.App.TILE_SIZE;

public class Sprite {

  /**
   * The type of object this sprite represents in-game.
   * */
  private String spriteType;

  /**
   * The filepath for the sprite's image.
   * */
  private String imageSource;
  /**
   * The actual sprite image.
   * */
  private Image image;

  /**
   * The sprite's X and Y coordinates.
   * */
  private float xCoordinate;
  private float yCoordinate;

  public Sprite(String imageSource, String spriteType, float x, float y) {
    this.xCoordinate = x;
    this.yCoordinate = y;
    this.imageSource = imageSource;
    this.spriteType = spriteType;

    try {
      this.image = new Image(this.imageSource);
    } catch (SlickException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  //TODO Later. This is not required at this stage of the project.
  public void update(Input input, int delta) {
  }

  /** Draws this sprite to the screen. */
  public void render(Graphics g) {
    g.drawImage(this.image, this.xCoordinate, this.yCoordinate);
  }

  /**
   * Determines whether an (x,y) coordinate lies inside a sprite.
   *
   * @param x The x-coordinate to be checked.
   * @param y The y-coordinate to be checked.
   * @return True if it lies inside the sprite, false otherwise.
   * */
  public boolean pointIsInside(float x, float y) {
    // x values increase as you move right across the window
    float leftEdge = this.getxCoordinate();
    float rightEdge = this.getxCoordinate() + TILE_SIZE;

    // y values increase as you move down the window
    float topEdge = this.getyCoordinate();
    float bottomEdge = this.getyCoordinate() + TILE_SIZE;

    return (leftEdge < x) && (x < rightEdge) && (topEdge < y) && (y < bottomEdge);
  }

  public float getxCoordinate() {
    return this.xCoordinate;
  }

  public void setxCoordinate(float xCoordinate) {
    this.xCoordinate = xCoordinate;
  }

  public float getyCoordinate() {
    return this.yCoordinate;
  }

  public void setyCoordinate(float yCoordinate) {
    this.yCoordinate = yCoordinate;
  }

  public String getSpriteType() {
    return this.spriteType;
  }

  @Override
  public String toString() {
    return "Sprite{" +
        "imageSource='" + imageSource + '\'' +
        "xCoordinate='" + xCoordinate + '\'' +
        "yCoordinate='" + yCoordinate + '\'' +
        '}';
  }
}
