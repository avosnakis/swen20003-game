/**
 * Sample Project for SWEN20003: Object Oriented Software Development 2017
 * by Eleanor McMurtry
 */
package project1;

import java.util.ArrayList;
import java.util.Arrays;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class World {
  private static final int NO_INDEX = -1;

  private ArrayList<Sprite> sprites;
  private int[][][] spriteIndices;

  public World() {
    this.sprites = Loader.loadSprites("res/levels/0.lvl");

    this.spriteIndices = new int[Loader.getWorldWidth()][Loader.getWorldWidth()][5];
    for (int[][] plane : this.spriteIndices) {
      for (int[] row : plane) {
        Arrays.fill(row, NO_INDEX);
      }
    }

    for (int i = 0; i < this.sprites.size(); i++) {
      this.insertIndex(i, (int)this.sprites.get(i).getX(), (int)this.sprites.get(i).getY());
    }
  }

  public void update(Input input, int delta) {
    for (Sprite sprite : this.sprites) {
      if (sprite != null) {
        sprite.update(input, delta, this);
      }
    }
  }

  public void render(Graphics g) {
    for (Sprite sprite : this.sprites) {
      if (sprite != null) {
        sprite.render(g);
      }
    }
  }

  public boolean isBlocked(int x, int y, int dir) {
    boolean cannotMove = true;
    for (int i = 0; i < this.spriteIndices[x][y].length && this.spriteIndices[x][y][i] != NO_INDEX; i++) {
      int index = this.spriteIndices[x][y][i];
      switch (this.sprites.get(index).getSpriteCategory()) {
        case "character":
          break;
        case "tile":
          cannotMove = !this.sprites.get(index).isPassable();
          break;
        case "block":
          this.sprites.get(index).moveToDestination
      }
    }
    return true;
  }

  private void insertIndex(int index, int x, int y) {
    int i = 0;
    while (this.spriteIndices[x][y][i] != NO_INDEX) {
      i++;
    }
    this.spriteIndices[x][y][i] = index;
  }
}
