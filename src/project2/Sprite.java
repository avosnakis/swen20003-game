package project2;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.function.BiPredicate;

/**
 * Sprite superclass. Records all information regarding the Sprite's whereabouts in the world grid and on the window,
 * whether other Sprites can move through them, their category and type, and their image.
 * <p>
 * Template taken from:
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 *
 * @author Alex Vosnakis 743936
 */
public abstract class Sprite {

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

  /**
   * Update this sprite.
   *
   * @param keys  The keys the player has pressed.
   * @param delta The time since the last frame (ignored).
   * @param world The world this sprite is in.
   */
  public void update(ArrayList<Integer> keys, int delta, World world) {
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

  /**
   * @return A predicate testing whether a sprite is of a given type.
   */
  public static BiPredicate<Sprite, String> isOfType() {
    return ((sprite, s) -> sprite.type.equals(s));
  }

  /**
   * @return A predicate testing whether a sprite is of a given category.
   */
  public static BiPredicate<Sprite, String> isOfCategory() {
    return ((sprite, s) -> sprite.category.equals(s));
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

  public boolean isPassable() {
    return passable;
  }

  public void setPassable(boolean passable) {
    this.passable = passable;
  }

  public Position<Integer> getCellPosition() {
    return cellPosition;
  }
}
