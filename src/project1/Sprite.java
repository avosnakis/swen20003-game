package project1;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import static project1.PlayerCharacter.UP;
import static project1.PlayerCharacter.DOWN;
import static project1.PlayerCharacter.LEFT;
import static project1.PlayerCharacter.RIGHT;

import static project1.App.TILE_SIZE;


/**
 * Class for the sprites.
 * Records the sprite's image, type, coordinates on the window and coordinates in the world grid.
 */
public class Sprite {

  /**
   * The type of the sprite.
   */
  private String spriteType;

  /**
   * The actual sprite image.
   */
  private Image image;

  /**
   * The sprite's X and Y coordinates on the window.
   */
  private float xCoordinate;
  private float yCoordinate;

  /**
   * The sprite's X and Y coordinates in the world grid.
   */
  private int xCell;
  private int yCell;

  /**
   * Instantiates a sprite.
   *
   * @param imageSource The filepath of this sprite's image.
   * @param spriteType  The type of sprite being created.
   * @param x           The x-coordinate on the window of this sprite.
   * @param y           The y-coordinate on the window of this sprite.
   * @param xCell       The x-coordinate of the cell in the world grid of this sprite.
   * @param yCell       The y-coordinate of the cell in the world grid of this sprite.
   */
  public Sprite(String imageSource, String spriteType, float x, float y, int xCell, int yCell) {
    this.xCoordinate = x;
    this.yCoordinate = y;

    this.xCell = xCell;
    this.yCell = yCell;

    this.spriteType = spriteType;

    try {
      this.image = new Image(imageSource);
    } catch (SlickException e) {
      e.printStackTrace();
      System.exit(1);
    }
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
   *
   * @param g The Slick graphics object this sprite is being drawn to.
   */
  public void render(Graphics g) {
    g.drawImage(this.image, this.xCoordinate, this.yCoordinate);
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

  public int getxCell() {
    return this.xCell;
  }

  public int getyCell() {
    return this.yCell;
  }

  public String getSpriteType() {
    return this.spriteType;
  }

  @Override
  public String toString() {
    return "Sprite{" +
        ", spriteType='" + this.spriteType + '\'' +
        ", image=" + this.image +
        ", xCoordinate=" + this.xCoordinate +
        ", yCoordinate=" + this.yCoordinate +
        ", xCell=" + this.xCell +
        ", yCell=" + this.yCell +
        '}';
  }
}
