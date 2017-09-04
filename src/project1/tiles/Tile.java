package project1.tiles;

import org.newdawn.slick.Graphics;
import project1.Sprite;

public abstract class Tile {

  private boolean passable;

  private Sprite sprite;

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
}
