package project1.tiles;

import org.newdawn.slick.Graphics;

import project1.Sprite;

/**
 * Abstract superclass for all world tiles.
 */
public abstract class Tile {

  /**
   * Whether or not this tile is passable.
   */
  private boolean passable;

  /**
   * This tile's sprite.
   */
  private Sprite sprite;

  /**
   * The cell in the world this tile occupies.
   */
  private int yCell;
  private int xCell;

  /**
   * Instantiates a tile.
   *
   * @param sprite This tile's sprite.
   * @param xCell The x-coordinate in the world of this cell.
   * @param yCell The y-coordinate in the world of this cell.
   */
  public Tile(Sprite sprite, int xCell, int yCell) {
    this.sprite = sprite;
    this.yCell = yCell;
    this.xCell = xCell;
  }

  /**
   * Renders this tile to the screen.
   *
   * @param g The Slick graphics object this tile is being rendered to.
   */
  public void render(Graphics g) {
    this.sprite.render(g);
  }

  public boolean isPassable() {
    return this.passable;
  }

  public void setPassable(boolean passable) {
    this.passable = passable;
  }

  public int getyCell() {
    return this.yCell;
  }

  public int getxCell() {
    return this.xCell;
  }

  @Override
  public String toString() {
    return "Tile{" +
        "passable=" + passable +
        ", sprite=" + sprite +
        ", yCell=" + yCell +
        ", xCell=" + xCell +
        '}';
  }
}
