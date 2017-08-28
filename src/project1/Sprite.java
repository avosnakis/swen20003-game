package project1;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import static project1.Player.UP;
import static project1.Player.DOWN;
import static project1.Player.LEFT;
import static project1.Player.RIGHT;

import static project1.App.TILE_SIZE;

public class Sprite {

  /**
   * The type of object this sprite represents in-game.
   */
  private String spriteType;

  /**
   * The filepath for the sprite's image.
   */
  private String imageSource;

  /**
   * The actual sprite image.
   */
  private Image image;

  /**
   * The sprite's X and Y coordinates.
   */
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

  @Override
  public String toString() {
    return "Sprite{" +
        "spriteType='" + this.spriteType + '\'' +
        ", imageSource='" + this.imageSource + '\'' +
        ", xCoordinate='" + this.xCoordinate + '\'' +
        ", yCoordinate='" + this.yCoordinate + '\'' +
        '}';
  }

  /**
   * Update a sprite based on user input.
   *
   * @param input The Slick user input object.
   * @param delta Time passed since the last frame (milliseconds).
   */
  public void update(Input input, int delta) {
    // TODO Later. This is not required at this stage of the project.
  }

  /**
   * Draws this sprite to the screen.
   */
  public void render(Graphics g) {
    g.drawImage(this.image, this.xCoordinate, this.yCoordinate);
  }

  /**
   * Determines whether an (x,y) coordinate lies inside a sprite.
   *
   * @param x The x-coordinate to be checked.
   * @param y The y-coordinate to be checked.
   * @return True if it lies inside the sprite, false otherwise.
   */
  public boolean pointIsInside(float x, float y) {
    // x values increase as you move right across the window
    float leftEdge = this.getxCoordinate();
    float rightEdge = this.getxCoordinate() + TILE_SIZE;

    // y values increase as you move down the window
    float topEdge = this.getyCoordinate();
    float bottomEdge = this.getyCoordinate() + TILE_SIZE;

    return (leftEdge < x) && (x < rightEdge) && (topEdge < y) && (y < bottomEdge);
  }

  /**
   * Increments the position of a sprite by one tile in the specified direction.
   *
   * @param direction The Slick key code for the direction being moved in.
   */
  public void incrementByOneTile(char direction) {
    switch (direction) {
      case UP:
        this.yCoordinate -= TILE_SIZE;
        break;
      case DOWN:
        this.yCoordinate += TILE_SIZE;
        break;
      case LEFT:
        this.xCoordinate -= TILE_SIZE;
        break;
      case RIGHT:
        this.xCoordinate += TILE_SIZE;
        break;
      default:
        System.exit(1);
        break;
    }
  }

  /**
   * Determines whether this instance of a sprite is a wall.
   *
   * @return True if it is a wall, false otherwise.
   */
  public boolean isWall() {
    return this.spriteType.equals("wall");
  }

  /**
   * Determines whether this instance of a sprite is a player.
   *
   * @return True if it is a player, false otherwise.
   */
  public boolean isPlayer() {
    return this.spriteType.equals("player");
  }

  public float getxCoordinate() {
    return this.xCoordinate;
  }

  public float getyCoordinate() {
    return this.yCoordinate;
  }

}
