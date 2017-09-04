package project1.tiles;

import org.newdawn.slick.Graphics;
import project1.Sprite;

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

  public Tile(Sprite sprite, int xCell, int yCell) {
    this.sprite = sprite;
    this.yCell = yCell;
    this.xCell = xCell;
  }

  public void render(Graphics g) {
    this.sprite.render(g);
  }

  public boolean isPassable() {
    return passable;
  }

  public void setPassable(boolean passable) {
    this.passable = passable;
  }

  public int getyCell() {
    return yCell;
  }

  public int getxCell() {
    return xCell;
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
