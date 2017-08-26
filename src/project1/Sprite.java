package project1;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import static project1.App.TILE_SIZE;

public class Sprite {

  private String imageSource;
  private Image image;

  private float xCoordinate;
  private float yCoordinate;

  public Sprite(String imageSource, float x, float y) {
    this.xCoordinate = x * TILE_SIZE;
    this.yCoordinate = y * TILE_SIZE;
    this.imageSource = imageSource;

    System.out.println(this.imageSource);

    try {
      this.image = new Image(this.imageSource);
      this.image.draw();
    } catch (SlickException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  //TODO
  public void update(Input input, int delta) {
  }

  public void render(Graphics g) {
    g.drawImage(this.image, this.xCoordinate, this.yCoordinate);
  }

  public float getxCoordinate() {
    return xCoordinate;
  }

  public void setxCoordinate(float xCoordinate) {
    this.xCoordinate = xCoordinate;
  }

  public float getyCoordinate() {
    return yCoordinate;
  }

  public void setyCoordinate(float yCoordinate) {
    this.yCoordinate = yCoordinate;
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
